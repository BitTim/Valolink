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

create trigger set_updated_at_contracts
    before update on contracts
    for each row execute function update_updated_at();

create trigger set_updated_at_levels
    before update on levels
    for each row execute function update_updated_at();

create trigger set_updated_at_match_details
    before update on match_details
    for each row execute function update_updated_at();

create trigger set_updated_at_matches
    before update on matches
    for each row execute function update_updated_at();

-- Automatically create user and flags when auth user gets created
create or replace function create_user_and_flags()
returns trigger
security definer
set search_path = public
as $$
begin
    insert into users (id) values (new.id);
    insert into flags (uid) values (new.id);
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
    new.accepted := true;
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
        update follows set accepted = true
        where following = new.id and accepted = false;
    end if;
    return new;
end;
$$ language plpgsql;

create trigger on_user_unprivate
    after update on users
    for each row execute function accept_pending_follows_on_unprivate();

-- Clean up match_details when last match referencing an entry is deleted
create or replace function cleanup_match_details()
returns trigger
set search_path = public
as $$
declare
    next_owner uuid;
begin
    if not exists (select 1 from matches where details = old.details) then
        -- No matches linked to details, delete orphaned match details
        delete from match_details where id = old.details;
    elsif old.is_owner = true then
        -- Owner deleted their match, assign to the oldest remaining match
        select uid into next_owner
        from matches
        where details = old.details
        order by created_at asc
        limit 1;

        update matches
        set is_owner = true
        where uid = next_owner
        and details = old.details;
    end if;

    return old;
end;
$$ language plpgsql;

create trigger on_match_delete
    after delete on matches
    for each row execute function cleanup_match_details();