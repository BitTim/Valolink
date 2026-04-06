-- Tests for RLS policies on the levels table using seed data

\set alice          00000000-0000-0000-0000-000000000001
\set bob            00000000-0000-0000-0000-000000000002
\set carol          00000000-0000-0000-0000-000000000003
\set dave           00000000-0000-0000-0000-000000000004
\set erin           00000000-0000-0000-0000-000000000005
\set fred           00000000-0000-0000-0000-000000000006
\set grace          00000000-0000-0000-0000-000000000007
\set hans           00000000-0000-0000-0000-000000000008
\set nonexisting    00000000-0000-0000-0000-000000000099

\set contract1      aaa00000-0000-0000-0000-000000000001
\set contract2      aaa00000-0000-0000-0000-000000000002
\set contract3      aaa00000-0000-0000-0000-000000000003

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
    $$ select * from levels $$,
    'Anon cannot read any levels'
);

select throws_ok(
    $$ insert into levels (uid, contract, level) values ('$$ || :'alice' || $$', '$$ || :'contract1' || $$', '$$ || :'level1' || $$') $$,
    42501, -- RLS violation
    null,
    'Anon cannot insert levels'
);

update levels set is_purchased = true where uid = :'alice' and contract = :'contract1' and level = :'level1';
delete from levels where uid = :'alice' and contract = :'contract1' and level = :'level2';
set local role postgres;

select is(
    (select is_purchased from levels where uid = :'alice' and contract = :'contract1' and level = :'level1'),
    false,
    'Anon cannot update any levels'
);

select ok(
    exists(select 1 from levels where uid = :'alice' and contract = :'contract1' and level = :'level2'),
    'Anon cannot delete any levels'
);
-- endregion:   anon

-- region:      authenticated
-- Impersonate Alice
set local role authenticated;
set local request.jwt.claims to '{"sub": "00000000-0000-0000-0000-000000000001"}';

-- region:      select
select ok(
    exists(select 1 from levels where uid = :'alice'),
    'Can read own levels'
);

select ok(
    exists(select 1 from levels where uid = :'bob'),
    'Can read public profiles levels'
);

select ok(
    exists(select 1 from levels where uid = :'carol'),
    'Can read accepted private profiles levels'
);

select ok(
    not exists(select 1 from levels where uid = :'dave'),
    'Cannot read pending private profiles levels'
);

select ok(
    not exists(select 1 from levels where uid = :'fred'),
    'Cannot read unfollowed profiles levels'
);
-- endregion:   select

-- region:      insert
select lives_ok(
    $$ insert into levels (uid, contract, level) values ('$$ || :'alice' || $$', '$$ || :'contract1' || $$', '$$ || :'level4' || $$') $$,
    'Can insert own levels'
);

select throws_ok(
    $$ insert into levels (uid, contract, level) values ('$$ || :'bob' || $$', '$$ || :'contract1' || $$', '$$ || :'level4' || $$') $$,
    42501, -- RLS violation
    null,
    'Cannot insert levels on behalf of another user'
);
-- endregion:   insert

-- region:      update
update levels set is_purchased = true where uid = :'alice' and contract = :'contract1' and level = :'level1';
select is(
    (select is_purchased from levels where uid = :'alice' and contract = :'contract1' and level = :'level1'),
    true,
    'Can update own levels'
);

update levels set is_purchased = true where uid = :'bob' and contract = :'contract1' and level = :'level1';
select is(
    (select is_purchased from levels where uid = :'bob' and contract = :'contract1' and level = :'level1'),
    false,
    'Cannot update levels on behalf of another user'
);
-- endregion:   update

-- region:      delete
delete from levels where uid = :'alice' and contract = :'contract1' and level = :'level3';
select ok(
    not exists(select 1 from levels where uid = :'alice' and contract = :'contract1' and level = :'level3'),
    'Can delete own levels'
);

delete from levels where uid = :'bob' and contract = :'contract1' and level = :'level2';
select ok(
    exists(select 1 from levels where uid = :'bob' and contract = :'contract1' and level = :'level2'),
    'Cannot delete levels on behalf of another user'
);
-- endregion:   delete

select * from finish();
rollback;