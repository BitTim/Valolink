-- Automatically update updated_at when row is inserted or modified
create or replace function update_updated_at()
returns trigger
set search_path = public
as $$
begin
    new.updated_at = now();
    return new;
end;
$$ language plpgsql;

create trigger set_updated_at_users
    before update on users
    for each row execute function update_updated_at();

create trigger set_updated_at_flags
    before update on flags
    for each row execute function update_updated_at();

create trigger set_updated_at_follows
    before update on follows
    for each row execute function update_updated_at();

create trigger set_updated_at_progressions
    before update on progressions
    for each row execute function update_updated_at();

create trigger set_updated_at_purchased_levels
    before update on purchased_levels
    for each row execute function update_updated_at();

create trigger set_updated_at_activities
    before update on activities
    for each row execute function update_updated_at();

create trigger set_updated_at_matches
    before update on matches
    for each row execute function update_updated_at();

create trigger set_updated_at_match_participants
    before update on match_participants
    for each row execute function update_updated_at();

-- Automatically create user and flags when auth user gets created
create or replace function create_user_and_flags()
returns trigger
security definer
set search_path = public
as $$
begin
    insert into users (id) values (new.id);
    insert into flags (user_id) values (new.id);
    return new;
end;
$$ language plpgsql;

create trigger on_user_created
    after insert on auth.users
    for each row execute function create_user_and_flags();

-- Automatically accept follows if profile is public
create or replace function set_follow_accepted()
returns trigger
set search_path = public
as $$
declare
  v_is_private boolean;
begin
  select is_private into v_is_private
  from users where id = new.following;

  if not v_is_private then
    new.relation_status := 'ACCEPTED';
  end if;

  return new;
end;
$$ language plpgsql;

create trigger on_follow_insert
    before insert on follows
    for each row execute function set_follow_accepted();

-- Bulk accept pending follows when profile goes public
create or replace function accept_pending_follows_on_unprivate()
returns trigger
set search_path = public
as $$
begin
    if old.is_private = true and new.is_private = false then
        update follows set relation_status = 'ACCEPTED'
        where following = new.id and relation_status = 'PENDING';
    end if;
    return new;
end;
$$ language plpgsql;

create trigger on_user_unprivate
    after update on users
    for each row execute function accept_pending_follows_on_unprivate();

-- Clean up matches when last match referencing an entry is deleted
create or replace function cleanup_matches()
returns trigger
set search_path = public
as $$
declare
    next_owner uuid;
begin
    if not exists (select 1 from match_participants where match = old.match) then
        -- No match_participants linked to match, delete orphaned match match
        delete from matches where id = old.match;
    elsif old.is_owner = true then
        -- Owner deleted their match, assign to the oldest remaining match
        select user_id into next_owner
        from match_participants
        where match = old.match
        order by created_at asc
        limit 1;

        update match_participants
        set is_owner = true
        where user_id = next_owner
        and match = old.match;
    end if;

    return old;
end;
$$ language plpgsql;

create trigger on_match_delete
    after delete on match_participants
    for each row execute function cleanup_matches();

-- Update progression xp when a new activity is inserted
-- create or replace function update_progression_on_activity_insert()
-- returns trigger
-- set search_path = public
-- as $$
-- declare
--     v_progression uuid;
-- begin
--     select progression into v_progression
--     from activities where id = new.activity;
--     update progressions
--     set total_xp = total_xp + new.xp
--     where user_id = new.user_id and progression = v_progression;
--     return new;
-- end;
-- $$ language plpgsql;