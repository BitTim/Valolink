-- Tests for RLS policies on the matches table using seed data

\set alice          00000000-0000-0000-0000-000000000001
\set bob            00000000-0000-0000-0000-000000000002
\set carol          00000000-0000-0000-0000-000000000003
\set dave           00000000-0000-0000-0000-000000000004
\set erin           00000000-0000-0000-0000-000000000005
\set fred           00000000-0000-0000-0000-000000000006
\set grace          00000000-0000-0000-0000-000000000007
\set hans           00000000-0000-0000-0000-000000000008
\set nonexisting    00000000-0000-0000-0000-000000000099

\set details1       ccc00000-0000-0000-0000-000000000001
\set details2       ccc00000-0000-0000-0000-000000000002
\set details3       ccc00000-0000-0000-0000-000000000003
\set details4       ccc00000-0000-0000-0000-000000000004
\set details5       ccc00000-0000-0000-0000-000000000005
\set details6       ccc00000-0000-0000-0000-000000000006

begin;
select plan(15);

-- region:      anon
-- Impersonate anon role
set local role anon;

select is_empty(
    $$ select * from matches $$,
    'Anon cannot read any matches'
);

select throws_ok(
    $$ insert into matches (uid, details) values ('$$ || :'alice' || $$', '$$ || :'details1' || $$') $$,
    42501, -- RLS violation
    null,
    'Anon cannot insert matches'
);

update matches set xp = 100 where uid = :'alice' and details = :'details1';
delete from matches where uid = :'alice' and details = :'details2';
set local role postgres;

select is(
    (select xp from matches where uid = :'alice' and details = :'details1'),
    4200,
    'Anon cannot update any matches'
);

select ok(
    exists(select 1 from matches where uid = :'alice' and details = :'details2'),
    'Anon cannot delete any matches'
);
-- endregion:   anon

-- region:      authenticated
-- Impersonate Alice
set local role authenticated;
set local request.jwt.claims to '{"sub": "00000000-0000-0000-0000-000000000001"}';

-- region:      select
select ok(
    exists(select 1 from matches where uid = :'alice'),
    'Can read own matches'
);

select ok(
    exists(select 1 from matches where uid = :'bob'),
    'Can read public profiles matches'
);

select ok(
    exists(select 1 from matches where uid = :'carol'),
    'Can read accepted private profiles matches'
);

select ok(
    not exists(select 1 from matches where uid = :'dave'),
    'Cannot read pending private profiles matches'
);

select ok(
    not exists(select 1 from matches where uid = :'fred'),
    'Cannot read private profiles matches'
);
-- endregion:   select

-- region:      insert
select lives_ok(
    $$ insert into matches (uid, details) values ('$$ || :'alice' || $$', '$$ || :'details4' || $$') $$,
    'Can insert own matches'
);

select throws_ok(
    $$ insert into matches (uid, details) values ('$$ || :'bob' || $$', '$$ || :'details4' || $$') $$,
    42501, -- RLS violation
    null,
    'Cannot insert matches on behalf of another user'
);
-- endregion:   insert

-- region:      update
update matches set xp = 100 where uid = :'alice' and details = :'details1';
select is(
    (select xp from matches where uid = :'alice' and details = :'details1'),
    100,
    'Can update own matches'
);

update matches set xp = 100 where uid = :'bob' and details = :'details3';
select is(
    (select xp from matches where uid = :'bob' and details = :'details3'),
    3800,
    'Cannot update matches on behalf of another user'
);
-- endregion:   update

-- region:      delete
delete from matches where uid = :'alice' and details = :'details2';
select ok(
    not exists(select 1 from matches where uid = :'alice' and details = :'details2'),
    'Can delete own matches'
);

delete from matches where uid = :'bob' and details = :'details3';
select ok(
    exists(select 1 from matches where uid = :'bob' and details = :'details3'),
    'Cannot delete matches on behalf of another user'
);
-- endregion:   delete
-- endregion:   authenticated

select * from finish();
rollback;