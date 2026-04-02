create or replace function public.visible_users()
returns setof uuid
language sql
security definer
stable
set search_path = public
as $$
    select id from users where is_private = false
    union
    select following from follows
    where follower = (select auth.uid())
    and accepted = true
$$;

alter table users enable row level security;

create policy "Authenticated user has full access to own profile"
on users
to authenticated
using ((select auth.uid()) = id)
with check ((select auth.uid()) = id);

create policy "Authenticated users can view public or followed profiles"
on users for select
to authenticated
using (
    id in (select public.visible_users())
);

alter table follows enable row level security;

create policy "Authenticated user has full access to own following"
on follows
to authenticated
using ((select auth.uid()) = follower)
with check ((select auth.uid()) = follower);

create policy "Authenticated users can view follows of public or followed users"
on follows for select
to authenticated
using (
    follower in (select public.visible_users()) and
    following in (select public.visible_users())
);

alter table agents enable row level security;

create policy "Authenticated user has full access to own agents"
on agents
to authenticated
using ((select auth.uid()) = uid)
with check ((select auth.uid()) = uid);

create policy "Authenticated users can view agents of public or followed users"
on agents for select
to authenticated
using (
    uid in (select public.visible_users())
);

alter table contracts enable row level security;

create policy "Authenticated user has full access to own contracts"
on contracts
to authenticated
using ((select auth.uid()) = uid)
with check ((select auth.uid()) = uid);

create policy "Authenticated users can view contracts of public or followed users"
on contracts for select
to authenticated
using(
    uid in (select public.visible_users())
);

alter table levels enable row level security;

create policy "Authenticated user has full access to own levels"
on levels
to authenticated
using ((select auth.uid()) = uid)
with check ((select auth.uid()) = uid);

create policy "Authenticated users can view levels of public or followed users"
on levels for select
to authenticated
using (
    uid in (select public.visible_users())
);

alter table match_details enable row level security;

create policy "Authenticated user has full access to owned match details"
on match_details
to authenticated
using ((select auth.uid()) = owner)
with check ((select auth.uid()) = owner);

create policy "Authenticated users can view match_details depending on the most permissive visibility of all participants"
on match_details for select
to authenticated
using (
    id in (
        select details from matches where uid in (
            select public.visible_users()
        )
    )
);

alter table matches enable row level security;

create policy "Authenticated user has full access to owned matches"
on matches
to authenticated
using ((select auth.uid()) = uid)
with check ((select auth.uid()) = uid);

create policy "Authenticated users can view matches of public or followed users"
on matches for select
to authenticated
using (
    uid in (select public.visible_users())
);

alter table rel_match_contract enable row level security;

create policy "Authenticated user has full access to owned match-contract junction"
on rel_match_contract
to authenticated
using ((select auth.uid()) = uid)
with check ((select auth.uid()) = uid);

create policy "Authenticated users can view match-contract junctions of public or followed users"
on rel_match_contract for select
to authenticated
using (
    uid in (select public.visible_users())
);