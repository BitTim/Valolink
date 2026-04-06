-- Tests for RLS policies on the contracts table using seed data

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

begin;
select plan(15);

-- region:      anon
-- Impersonate anon role
set local role anon;

select is_empty(
    $$ select * from contracts $$,
    'Anon cannot read any follows'
);

select throws_ok(
    $$ insert into contracts (uid, contract) values ('$$ || :'alice' || $$', '$$ || :'contract3' || $$') $$,
    42501, -- RLS violation
    null,
    'Anon cannot insert contracts'
);

update contracts set free_only = true where uid = :'alice' and contract = :'contract1';
delete from contracts where uid = :'alice' and contract = :'contract2';
set local role postgres;

select is(
    (select free_only from contracts where uid = :'alice' and contract = :'contract1'),
    false,
    'Anon cannot update any contracts'
);

select ok(
    exists(select 1 from contracts where uid = :'alice' and contract = :'contract2'),
    'Anon cannot delete any contracts'
);
-- endregion:   anon

-- region:      authenticated
-- Impersonate Alice
set local role authenticated;
set local request.jwt.claims to '{"sub": "00000000-0000-0000-0000-000000000001"}';

-- region:      select
select ok(
    exists(select 1 from contracts where uid = :'alice'),
    'Can read own contracts'
);

select ok(
    exists(select 1 from contracts where uid = :'erin'),
    'Can read public profiles contracts'
);

select ok(
    exists(select 1 from contracts where uid = :'carol'),
    'Can read accepted private profiles contracts'
);

select ok(
    not exists(select 1 from contracts where uid = :'dave'),
    'Cannot read pending private profiles contracts'
);

select ok(
    not exists(select 1 from contracts where uid = :'fred'),
    'Cannot read unfollowed profiles contracts'
);
-- endregion:   select

-- region:      insert
select lives_ok(
    $$ insert into contracts (uid, contract) values ('$$ || :'alice' || $$', '$$ || :'contract3' || $$') $$,
    'Can insert own contracts'
);

select throws_ok(
    $$ insert into contracts (uid, contract) values ('$$ || :'bob' || $$', '$$ || :'contract3' || $$') $$,
    42501, -- RLS violation
    null,
    'Cannot insert contracts on behalf of another user'
);
-- endregion:   insert

-- region:      update
update contracts set free_only = true where uid = :'alice' and contract = :'contract1';
select is(
    (select free_only from contracts where uid = :'alice' and contract = :'contract1'),
    true,
    'Can update own contracts'
);

update contracts set free_only = true where uid = :'bob' and contract = :'contract1';
select is(
    (select free_only from contracts where uid = :'bob' and contract = :'contract1'),
    false,
    'Cannot update contracts on behalf of another user'
);
-- endregion:   update

-- region:      delete
delete from contracts where uid = :'alice' and contract = :'contract2';
select ok(
    not exists(select 1 from contracts where uid = :'alice' and contract = :'contract2'),
    'Can delete own contracts'
);

delete from contracts where uid = :'bob' and contract = :'contract1';
select ok(
    exists(select 1 from contracts where uid = :'bob' and contract = :'contract1'),
    'Cannot delete contracts on behalf of another user'
);
-- endregion:   delete
-- endregion:   authenticated

select * from finish();
rollback;