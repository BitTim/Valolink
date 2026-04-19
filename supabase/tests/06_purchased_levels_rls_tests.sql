-- Tests for RLS policies on the purchased_levels table using seed data

\set alice          00000000-0000-0000-0000-000000000001
\set bob            00000000-0000-0000-0000-000000000002
\set carol          00000000-0000-0000-0000-000000000003
\set dave           00000000-0000-0000-0000-000000000004
\set erin           00000000-0000-0000-0000-000000000005
\set fred           00000000-0000-0000-0000-000000000006
\set grace          00000000-0000-0000-0000-000000000007
\set hans           00000000-0000-0000-0000-000000000008
\set nonexisting    00000000-0000-0000-0000-000000000099

\set progression1      aaa00000-0000-0000-0000-000000000001
\set progression2      aaa00000-0000-0000-0000-000000000002
\set progression3      aaa00000-0000-0000-0000-000000000003

\set level1         bbb00000-0000-0000-0000-000000000001
\set level2         bbb00000-0000-0000-0000-000000000002
\set level3         bbb00000-0000-0000-0000-000000000003
\set level4         bbb00000-0000-0000-0000-000000000004

begin;
select plan(15);

-- region:      anon
-- Impersonate anon role
set local role anon;

select is_empty(
    $$ select * from purchased_levels $$,
    'Anon cannot read any purchased_levels'
);

select throws_ok(
    $$ insert into purchased_levels (user_id, progression, level) values ('$$ || :'alice' || $$', '$$ || :'progression1' || $$', '$$ || :'level1' || $$') $$,
    42501, -- RLS violation
    null,
    'Anon cannot insert purchased_levels'
);

update purchased_levels set progression = :'progression3' where user_id = :'alice' and progression = :'progression1' and level = :'level1';
delete from purchased_levels where user_id = :'alice' and progression = :'progression1' and level = :'level2';
set local role postgres;

select is(
    (select progression from purchased_levels where user_id = :'alice' and progression = :'progression1' and level = :'level1'),
    :'progression1',
    'Anon cannot update any purchased_levels'
);

select ok(
    exists(select 1 from purchased_levels where user_id = :'alice' and progression = :'progression1' and level = :'level2'),
    'Anon cannot delete any purchased_levels'
);
-- endregion:   anon

-- region:      authenticated
-- Impersonate Alice
set local role authenticated;
set local request.jwt.claims to '{"sub": "00000000-0000-0000-0000-000000000001"}';

-- region:      select
select ok(
    exists(select 1 from purchased_levels where user_id = :'alice'),
    'Can read own purchased_levels'
);

select ok(
    exists(select 1 from purchased_levels where user_id = :'bob'),
    'Can read public profiles purchased_levels'
);

select ok(
    exists(select 1 from purchased_levels where user_id = :'carol'),
    'Can read accepted private profiles purchased_levels'
);

select ok(
    not exists(select 1 from purchased_levels where user_id = :'dave'),
    'Cannot read pending private profiles purchased_levels'
);

select ok(
    not exists(select 1 from purchased_levels where user_id = :'fred'),
    'Cannot read unfollowed profiles purchased_levels'
);
-- endregion:   select

-- region:      insert
select lives_ok(
    $$ insert into purchased_levels (user_id, progression, level) values ('$$ || :'alice' || $$', '$$ || :'progression1' || $$', '$$ || :'level4' || $$') $$,
    'Can insert own purchased_levels'
);

select throws_ok(
    $$ insert into purchased_levels (user_id, progression, level) values ('$$ || :'bob' || $$', '$$ || :'progression1' || $$', '$$ || :'level4' || $$') $$,
    42501, -- RLS violation
    null,
    'Cannot insert purchased_levels on behalf of another user'
);
-- endregion:   insert

-- region:      update
update purchased_levels set level = :'level4' where user_id = :'alice' and progression = :'progression1' and level = :'level1';
select ok(
    exists(select 1 from purchased_levels where user_id = :'alice' and progression = :'progression1' and level = :'level1'),
    'Cannot update own purchased_levels'
);

update purchased_levels set level = :'level4' where user_id = :'bob' and progression = :'progression1' and level = :'level1';
select ok(
    exists(select 1 from purchased_levels where user_id = :'bob' and progression = :'progression1' and level = :'level1'),
    'Cannot update purchased_levels on behalf of another user'
);
-- endregion:   update

-- region:      delete
delete from purchased_levels where user_id = :'alice' and progression = :'progression1' and level = :'level3';
select ok(
    not exists(select 1 from purchased_levels where user_id = :'alice' and progression = :'progression1' and level = :'level3'),
    'Can delete own purchased_levels'
);

delete from purchased_levels where user_id = :'bob' and progression = :'progression1' and level = :'level2';
select ok(
    exists(select 1 from purchased_levels where user_id = :'bob' and progression = :'progression1' and level = :'level2'),
    'Cannot delete purchased_levels on behalf of another user'
);
-- endregion:   delete

select * from finish();
rollback;