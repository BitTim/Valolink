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
    and status = 'ACCEPTED'
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
using (user_id in (select public.visible_users()));

create policy "User can insert own flags"
on flags for insert
to authenticated
with check ((select auth.uid()) = user_id);

create policy "User can update own flags"
on flags for update
to authenticated
using ((select auth.uid()) = user_id)
with check ((select auth.uid()) = user_id);

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
with check ((select auth.uid()) = follower and status = 'PENDING');

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
using (user_id in (select public.visible_users()));

create policy "User can insert own agents"
on agents for insert
to authenticated
with check ((select auth.uid()) = user_id);

create policy "User can delete own agents"
on agents for delete
to authenticated
using ((select auth.uid()) = user_id);

-- endregion:   agents
-- region:      progressions

alter table progressions enable row level security;

create policy "Users can view progressions of visible users"
on progressions for select
to authenticated
using(user_id in (select public.visible_users()));

create policy "User can insert own progressions"
on progressions for insert
to authenticated
with check ((select auth.uid()) = user_id);

create policy "User can update own progressions"
on progressions for update
to authenticated
using ((select auth.uid()) = user_id)
with check ((select auth.uid()) = user_id);

create policy "User can delete own progressions"
on progressions for delete
to authenticated
using ((select auth.uid()) = user_id);

-- endregion:   progressions
-- region:      purchased_levels

alter table purchased_levels enable row level security;

create policy "Users can view purchased levels of visible users"
on purchased_levels for select
to authenticated
using (user_id in (select public.visible_users()));

create policy "User can insert own purchased levels"
on purchased_levels for insert
to authenticated
with check ((select auth.uid()) = user_id);

create policy "User can update own purchased levels"
on purchased_levels for update
to authenticated
using ((select auth.uid()) = user_id)
with check ((select auth.uid()) = user_id);

create policy "User can delete own purchased levels"
on purchased_levels for delete
to authenticated
using ((select auth.uid()) = user_id);

-- endregion:   purchased_levels
-- region:      activities

alter table activities enable row level security;

create policy "Users can view activities of visible users"
on activities for select
to authenticated
using (user_id in (select public.visible_users()));

create policy "User can insert own activities"
on activities for insert
to authenticated
with check ((select auth.uid()) = user_id);

create policy "User can update own activities"
on activities for update
to authenticated
using ((select auth.uid()) = user_id)
with check ((select auth.uid()) = user_id);

create policy "User can delete own activities"
on activities for delete
to authenticated
using ((select auth.uid()) = user_id);

-- endregion:   activities
-- region:      matches

alter table matches enable row level security;

create policy "Users can view all matches"
on matches for select
to authenticated
using (true);

create policy "Owner can insert own matches"
on matches for insert
to authenticated
with check (
    exists (
        select 1 from match_participants
        where user_id = (select auth.uid())
        and match = matches.id
        and is_owner = true
    )
);

create policy "Owner can update own matches"
on matches for update
to authenticated
using (
    exists (
        select 1 from match_participants
        where user_id = (select auth.uid())
        and match = matches.id
        and is_owner = true
    )
)
with check (
    exists (
        select 1 from match_participants
        where user_id = (select auth.uid())
        and match = matches.id
        and is_owner = true
    )
);

-- endregion:   matches
-- region:      match_participants

alter table match_participants enable row level security;

create policy "Users can view match participation of visible users"
on match_participants for select
to authenticated
using (user_id in (select public.visible_users()));

create policy "User can insert own match participation"
on match_participants for insert
to authenticated
with check((select auth.uid()) = user_id);

create policy "User can update own match participation"
on match_participants for update
to authenticated
using ((select auth.uid()) = user_id)
with check ((select auth.uid()) = user_id);

create policy "User can delete own match participation"
on match_participants for delete
to authenticated
using ((select auth.uid()) = user_id);

-- endregion:   match_participants