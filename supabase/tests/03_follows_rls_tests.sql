-- Tests for RLS policies on the follows table using seed data

\set alice          00000000-0000-0000-0000-000000000001
\set bob            00000000-0000-0000-0000-000000000002
\set carol          00000000-0000-0000-0000-000000000003
\set dave           00000000-0000-0000-0000-000000000004
\set erin           00000000-0000-0000-0000-000000000005
\set fred           00000000-0000-0000-0000-000000000006
\set grace          00000000-0000-0000-0000-000000000007
\set hans           00000000-0000-0000-0000-000000000008
\set nonexisting    00000000-0000-0000-0000-000000000099

begin;
select plan(27);

-- region:      anon
-- Impersonate anon role
set local role anon;

select is_empty(
    $$ select * from follows $$,
    'Anon cannot read any follows'
);

select throws_ok(
    $$ insert into follows (follower, following) values ('$$ || :'fred' || $$', '$$ || :'alice' || $$') $$,
    42501, -- RLS violation
    null,
    'Anon cannot insert follows'
);

update follows set relation_status = 'ACCEPTED' where follower = :'alice' and following = :'dave';
delete from follows where follower = :'bob' and following = :'alice';
set local role postgres;

select is(
    (select relation_status from follows where follower = :'alice' and following = :'dave'),
    'PENDING',
    'Anon cannot update any follows'
);

select ok(
    exists(select 1 from follows where follower = :'bob' and following = :'alice'),
    'Anon cannot delete any follows'
);
-- endregion:   anon

-- region:      authenticated
-- Impersonate Alice
set local role authenticated;
set local request.jwt.claims to '{"sub": "00000000-0000-0000-0000-000000000001"}';

-- region:      select
select ok(
    exists(select 1 from follows where follower = :'alice'),
    'Can read own follows'
);

select ok(
    exists(select 1 from follows where follower = :'fred' and following = :'alice'),
    'Can read any profiles follows if being followed'
);

select ok(
    exists(select 1 from follows where follower = :'bob' and following = :'erin'),
    'Can read a public profiles public follows'
);

select ok(
    exists(select 1 from follows where follower = :'erin' and following = :'carol'),
    'Can read a public profiles private follows when accepted'
);

select ok(
    not exists(select 1 from follows where follower = :'bob' and following = :'dave'),
    'Cannot read a public profiles private follows when pending'
);

select ok(
    not exists(select 1 from follows where follower = :'bob' and following = :'fred'),
    'Cannot read a public profiles private follows when not following'
);

select ok(
    exists(select 1 from follows where follower = :'carol' and following = :'bob'),
    'Can read an accepted private profiles public follows'
);

select ok(
    exists(select 1 from follows where follower = :'carol' and following = :'grace'),
    'Can read an accepted private profiles private follows when accepted'
);

select ok(
    not exists(select 1 from follows where follower = :'carol' and following = :'dave'),
    'Cannot read an accepted private profiles private follows when pending'
);

select ok(
    not exists(select 1 from follows where follower = :'carol' and following = :'fred'),
    'Cannot read an accepted private profiles private follows when not following'
);

select ok(
    not exists(select 1 from follows where follower = :'dave'),
    'Cannot read a pending private profiles follows'
);

select ok(
    not exists(select 1 from follows where follower = :'hans'),
    'Cannot read an unfollowed private profiles follows'
);
-- endregion:   select

-- region:      insert
select lives_ok(
    $$ insert into follows (follower, following) values ('$$ || :'alice' || $$', '$$ || :'fred' || $$') $$,
    'Can insert own follows'
);

select throws_ok(
    $$ insert into follows (follower, following, relation_status) values ('$$ || :'alice' || $$', '$$ || :'hans' || $$', 'ACCEPTED') $$,
    42501, -- RLS violation
    null,
    'Cannot insert relation_status as ACCEPTED'
);

select throws_ok(
    $$ insert into follows (follower, following) values ('$$ || :'grace' || $$', '$$ || :'alice' || $$') $$,
    42501, -- RLS violation
    null,
    'Cannot insert follows on behalf of another user'
);
-- endregion:   insert

-- region:      update
update follows set relation_status = 'ACCEPTED' where follower = :'alice' and following = :'dave';
select is(
    (select relation_status from follows where follower = :'alice' and following = :'dave'),
    'PENDING',
    'Cannot update relation_status in own follows'
);

update follows set follower = :'grace' where follower = :'alice' and following = :'bob';
update follows set following = :'grace' where follower = :'alice' and following = :'bob';
select ok(
    exists(select 1 from follows where follower = :'alice' and following = :'bob'),
    'Cannot update follower or following in own follows'
);

update follows set relation_status = 'BLOCKED' where follower = :'carol' and following = :'alice';
select is(
    (select relation_status from follows where follower = :'carol' and following = :'alice'),
    'BLOCKED',
    'Can update relation_status in another users follows when being followed'
);

select throws_ok(
    $$ update follows set follower = '$$ || :'grace' || $$' where follower = '$$ || :'bob' || $$' and following = '$$ || :'alice' || $$';
    update follows set following = '$$ || :'grace' || $$' where follower = '$$ || :'bob' || $$' and following = '$$ || :'alice' || $$'; $$,
    42501, -- RLS violation
    null,
    'Cannot update follower or following in another users follows when being followed'
);

update follows set relation_status = 'BLOCKED' where follower = :'bob' and following = :'erin';
select is(
    (select relation_status from follows where follower = :'bob' and following = :'erin'),
    'ACCEPTED',
    'Cannot update relation_status in another users follows when not being followed'
);
-- endregion:   update

-- region:      delete
delete from follows where follower = :'alice' and following = :'grace';
select ok(
    not exists(select 1 from follows where follower = :'alice' and following = :'grace'),
    'Can delete own follows'
);

delete from follows where follower = :'fred' and following = :'alice';
select ok(
    not exists(select 1 from follows where follower = :'fred' and following = :'alice'),
    'Can delete another users follows when being followed'
);

delete from follows where follower = :'carol' and following = :'bob';
select ok(
    exists(select 1 from follows where follower = :'carol' and following = :'bob'),
    'Cannot delete another users follows when not being followed'
);
-- endregion:   delete
-- endregion:   authenticated

select * from finish();
rollback;