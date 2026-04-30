-- More advanced triggers that span between user and content space

-- Automatically create user object and all related rows when auth user gets created
create or replace function create_user_object()
returns trigger
security definer
set search_path = public
language plpgsql as $$
begin
    insert into users (id) values (new.id);
    insert into flags (user_id) values (new.id);

    insert into agents (user_id, agent, owned_state)
    select new.id, valo_agents.uuid, case
        when valo_agents.is_base_content then 'BASE'
        else 'LOCKED'
    end
    from valo_agents
    on conflict (user_id, agent) do nothing;

    insert into progressions (user_id, progression, start_time, end_time, track_xp)
    select
        new.id,
        valo_progressions.uuid,
        case
            when valo_progressions.relation_type = 'SEASON' then valo_seasons.start_time
            when valo_progressions.relation_type = 'EVENT' then valo_events.start_time
            else null
        end,
        case
            when valo_progressions.relation_type = 'SEASON' then valo_seasons.end_time
            when valo_progressions.relation_type = 'EVENT' then valo_events.end_time
            else null
        end,
        valo_progressions.relation_type != 'AGENT'
    from valo_progressions
    left join valo_seasons on valo_seasons.uuid = valo_progressions.relation_uuid and valo_progressions.relation_type = 'SEASON'
    left join valo_events on valo_events.uuid = valo_progressions.relation_uuid and valo_progressions.relation_type = 'EVENT'
    on conflict (user_id, progression) do nothing;
    return new;
end;
$$;

create trigger on_user_created
    after insert on auth.users
    for each row execute function create_user_object();

-- Automatically create new agent when new valo_agents is available
create or replace function create_user_agent()
returns trigger
security definer
set search_path = public
language plpgsql as $$
begin
    insert into agents (user_id, agent, start_time, end_time, owned_state)
    select users.id, new.uuid, valo_agent_recruitments.start_time, valo_agent_recruitments.end_time, case
        when new.is_base_content then 'BASE'
        else 'LOCKED'
    end
    from users
    left join valo_agent_recruitments on valo_agent_recruitments.agent = new.uuid
    on conflict (user_id, agent) do nothing;
end;
$$;

create trigger on_valo_agent_insert
    after insert on valo_agents
    for each row execute function create_user_agent();

-- Automatically create new progression when new valo_progression is available
create or replace function create_user_progression()
returns trigger
security definer
set search_path = public
language plpgsql as $$
declare
    v_start_time timestamp with time zone;
    v_end_time timestamp with time zone;
begin
    if new.relation_type = 'SEASON' then
        select start_time, end_time
        into v_start_time, v_end_time
        from valo_seasons
        where uuid = new.relation_uuid;
    elsif new.relation_type = 'EVENT' then
        select start_time, end_time
        into v_start_time, v_end_time
        from valo_events
        where uuid = new.relation_uuid;
    end if;

    insert into progressions (user_id, progression, start_time, end_time, track_xp)
    select users.id, new.uuid, v_start_time, v_end_time, new.relation_type != 'AGENT'
    from users
    on conflict (user_id, progressions) do nothing;

    return new;
end;
$$;

create trigger on_valo_progression_insert
    after insert on valo_progressions
    for each row execute function create_user_progression();

-- Guard server-calculated total_xp from manual writes
create or replace function guard_total_xp()
returns trigger
security definer
set search_path = public
language plpgsql as $$
begin
    if new.total_xp != old.total_xp and current_setting('app.recalculating_xp') != 'true' then
        raise exception 'Total XP is only allowed to be modified server side';
    end if;
    return new;
end;
$$;

create trigger progression_total_xp_guard
    before update on progressions
    for each row execute function guard_total_xp();

create trigger agent_total_xp_guard
    before update on agents
    for each row execute function guard_total_xp();

-- Shared function for evaluating agent owned_state
create or replace function evaluate_agent_owned_state(
    p_owned_state text,
    p_total_xp integer,
    p_xp_offset integer,
    p_agent uuid
) returns text
security definer
set search_path = public
language plpgsql as $$
begin
    -- Don't touch base agents and manually unlocked agents
    if p_owned_state in ('BASE', 'PURCHASED') then
        return p_owned_state;
    end if;

    -- Check if agent has a recruitment
    if not exists(select 1 from valo_agent_recruitments where agent = p_agent) then
        return p_owned_state;
    end if;

    return case
        when p_total_xp + p_xp_offset >= (select xp from valo_agent_recruitments where agent = p_agent) then 'RECRUITED'
        else 'LOCKED'
    end;
end;
$$;

-- Guard BASE agents and re-evaluate owned state on xp_offset change
create or replace function on_agent_update()
returns trigger
security definer
set search_path = public
language plpgsql as $$
begin
    -- Guard BASE agents
    if old.owned_state in ('BASE') and new.owned_state is distinct from old.owned_state and current_setting('app.modify_base_agents') != 'true' then
        raise exception 'Base agents are not allowed to change their owned state';
    end if;

    -- Re-evaluate owned_state
    if new.xp_offset is distinct from old.xp_offset then
        new.owned_state = evaluate_agent_owned_state(
            new.owned_state,
            new.total_xp,
            new.xp_offset,
            new.agent
        );
    end if;

    return new;
end;
$$;

create trigger on_agent_update
    before update on agents
    for each row execute function on_agent_update();

-- Automatically recalculate xp for progressions and agent recruitments
create or replace function recalculate_xp()
returns trigger
security definer
set search_path = public
language plpgsql as $$
declare
    affected_uid    uuid;
    affected_time   timestamp with time zone;
begin
    affected_uid := coalesce(new.user_id, old.user_id);
    affected_time := coalesce(new.time, old.time);
    
    -- Set flag to bypass xp guard
    perform set_config('app.recalculating_xp', 'true', true);

    update progressions
    set total_xp = (
        select coalesce(sum(activities.xp), 0)
        from activities
        where activities.user_id = affected_uid
        and activities.type in ('MATCH', 'XP_CORRECTION')
        and activities.xp > 0
        and activities.time between progressions.start_time and progressions.end_time
    ) where progressions.user_id = affected_uid
    and progressions.track_xp = true
    and progressions.start_time is not null
    and (
        affected_time between progressions.start_time and progressions.end_time
        or old.time between progressions.start_time and progressions.end_time
    );

    update agents
    set total_xp = (
        select coalesce(sum(activities.xp), 0)
        from activities
        where activities.user_id = affected_uid
        and activities.type in ('MATCH', 'XP_CORRECTION')
        and activities.xp > 0
        and activities.time between agents.start_time and agents.end_time
    ) where agents.user_id = affected_uid
    and agents.owned_state in ('LOCKED', 'RECRUITED', 'PURCHASED')
    and agents.start_time is not null
    and (
        affected_time between agents.start_time and agents.end_time
        or old.time between agents.start_time and agents.end_time
    );

    update agents
    set owned_state = (
        evaluate_agent_owned_state(
            agents.owned_state,
            agents.total_xp,
            agents.xp_offset,
            agents.agent
        )
    ) where agents.user_id = affected_uid
    and agents.owned_state in ('LOCKED', 'RECRUITED')
    and agents.start_time is not null
    and (
        affected_time between agents.start_time and agents.end_time
        or old.time between agents.start_time and agents.end_time
    );

    -- Reset flag to re-activate xp guard
    perform set_config('app.recalculating_xp', 'false', true);
    return coalesce(new, old);
end;
$$;

create trigger on_activity_change_recalculate_xp
    after insert or update or delete on activities
    for each row execute function recalculate_xp();