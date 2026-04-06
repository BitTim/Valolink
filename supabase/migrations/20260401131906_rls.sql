-- region:      functions
create or replace function public.visible_users()
returns setof uuid
language sql
security definer
stable
set search_path = public
as $$
    select auth.uid()
    union
    select id from users where is_private = false
    union
    select following from follows
    where follower = (select auth.uid())
    and accepted = true
$$;

create or replace function check_follow_update(p_follower uuid, p_following uuid)
returns boolean
language sql
security definer
stable
as $$
    select exists (
        select 1 from follows
        where follower = p_follower
        and following = p_following
    );
$$;

CREATE OR REPLACE FUNCTION get_follow(p_follower uuid, p_following uuid)
RETURNS follows
LANGUAGE sql
SECURITY DEFINER
STABLE
AS $$
  SELECT * FROM follows
  WHERE follower = p_follower
    AND following = p_following;
$$;
-- endregion:   functions

-- region:      users
alter table users enable row level security;

create policy "Users can view all profiles"
on users for select
to authenticated
using (true);

create policy "User can insert own profile"
on users for insert
to authenticated
with check ((select auth.uid()) = id);

create policy "User can update own profile"
on users for update
to authenticated
using ((select auth.uid()) = id)
with check ((select auth.uid()) = id);
-- endregion:   users

-- region:      flags
alter table flags enable row level security;

create policy "Users can view flags of visible users"
on flags for select
to authenticated
using (uid in (select public.visible_users()));

create policy "User can insert own flags"
on flags for insert
to authenticated
with check ((select auth.uid()) = uid);

create policy "User can update own flags"
on flags for update
to authenticated
using ((select auth.uid()) = uid)
with check ((select auth.uid()) = uid);
-- endregion:   flags

-- region:      follows
alter table follows enable row level security;

create policy "Users can view own follows and of visible users"
on follows for select
to authenticated
using (
    follower = (select auth.uid()) or
    following = (select auth.uid()) or (
        follower in (select public.visible_users()) and
        following in (select public.visible_users())
    )
);

create policy "User can insert own following"
on follows for insert
to authenticated
with check ((select auth.uid()) = follower);

create policy "User can update accepted when being followed"
on follows for update
to authenticated
using ((select auth.uid()) = following)
with check (
    following = (select auth.uid()) and
    check_follow_update(follower, following)
);

create policy "User can delete own following or if being followed"
on follows for delete
to authenticated
using ((select auth.uid()) = follower or (select auth.uid()) = following);
-- endregion:   follows

-- region:      agents
alter table agents enable row level security;

create policy "Users can view agents of visible users"
on agents for select
to authenticated
using (uid in (select public.visible_users()));

create policy "User can insert own agents"
on agents for insert
to authenticated
with check ((select auth.uid()) = uid);

create policy "User can delete own agents"
on agents for delete
to authenticated
using ((select auth.uid()) = uid);
-- endregion:   agents

-- region:      contracts
alter table contracts enable row level security;

create policy "Users can view contracts of visible users"
on contracts for select
to authenticated
using(uid in (select public.visible_users()));

create policy "User can insert own contracts"
on contracts for insert
to authenticated
with check ((select auth.uid()) = uid);

create policy "User can update own contracts"
on contracts for update
to authenticated
using ((select auth.uid()) = uid)
with check ((select auth.uid()) = uid);

create policy "User can delete own contracts"
on contracts for delete
to authenticated
using ((select auth.uid()) = uid);
-- endregion: contracts

-- region:      levels
alter table levels enable row level security;

create policy "Users can view levels of visible users"
on levels for select
to authenticated
using (uid in (select public.visible_users()));

create policy "User can insert own levels"
on levels for insert
to authenticated
with check ((select auth.uid()) = uid);

create policy "User can update own levels"
on levels for update
to authenticated
using ((select auth.uid()) = uid)
with check ((select auth.uid()) = uid);

create policy "User can delete own levels"
on levels for delete
to authenticated
using ((select auth.uid()) = uid);
-- endregion:   levels

-- region:      match_details
alter table match_details enable row level security;

create policy "Users can view all match details"
on match_details for select
to authenticated
using (true);

create policy "Owner can insert own match details"
on match_details for insert
to authenticated
with check (
    exists (
        select 1 from matches
        where uid = (select auth.uid())
        and details = match_details.id
        and is_owner = true
    )
);

create policy "Owner can update own match details"
on match_details for update
to authenticated
using (
    exists (
        select 1 from matches
        where uid = (select auth.uid())
        and details = match_details.id
        and is_owner = true
    )
)
with check (
    exists (
        select 1 from matches
        where uid = (select auth.uid())
        and details = match_details.id
        and is_owner = true
    )
);
-- endregion:   match_details

-- region:      matches
alter table matches enable row level security;

create policy "Users can view matches of visible users"
on matches for select
to authenticated
using (uid in (select public.visible_users()));

create policy "User can insert own matches"
on matches for insert
to authenticated
with check((select auth.uid()) = uid);

create policy "User can update own matches"
on matches for update
to authenticated
using ((select auth.uid()) = uid)
with check ((select auth.uid()) = uid);

create policy "User can delete own matches"
on matches for delete
to authenticated
using ((select auth.uid()) = uid);
-- endregion:   matches

-- region:      rel_match_contract
alter table rel_match_contract enable row level security;

create policy "Users can view match-contract junctions of visible users"
on rel_match_contract for select
to authenticated
using (
    uid in (select public.visible_users())
);

create policy "User can insert own match-contract junctions"
on rel_match_contract for insert
to authenticated
with check ((select auth.uid()) = uid);

create policy "User can update own match-contract junctions"
on rel_match_contract for update
to authenticated
using ((select auth.uid()) = uid)
with check ((select auth.uid()) = uid);

create policy "User can delete own match-contract junctions"
on rel_match_contract for delete
to authenticated
using ((select auth.uid()) = uid);
-- endregion:   rel_match_contract