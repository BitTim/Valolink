-- Tests for RLS policies on the rel_match_contract table using seed data

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
    $$ select * from rel_match_contract $$,
    'Anon cannot read any match-contract junctions'
);

select throws_ok(
    $$ insert into rel_match_contract (uid, contract, details) values ('$$ || :'alice' || $$', '$$ || :'contract2' || $$', '$$ || :'details1' || $$') $$,
    42501, -- RLS violation
    null,
    'Anon cannot insert match-contract junctions'
);

update rel_match_contract set details = :'details2' where uid = :'alice' and contract = :'contract2' and details = :'details5';
delete from rel_match_contract where uid = :'alice' and contract = :'contract1' and details = :'details1';
set local role postgres;

select ok(
    exists(select 1 from rel_match_contract where uid = :'alice' and contract = :'contract2' and details = :'details5'),
    'Anon cannot update any match-contract junctions'
);

select ok(
    exists(select 1 from rel_match_contract where uid = :'alice' and contract = :'contract1' and details = :'details1'),
    'Anon cannot delete any match-contract junctions'
);
-- endregion:   anon

-- region:      authenticated
-- Impersonate Alice
set local role authenticated;
set local request.jwt.claims to '{"sub": "00000000-0000-0000-0000-000000000001"}';

-- region:      select
select ok(
    exists(select 1 from rel_match_contract where uid = :'alice'),
    'Can read own match-contract junctions'
);

select ok(
    exists(select 1 from rel_match_contract where uid = :'bob'),
    'Can read public profiles match-contract junctions'
);

select ok(
    exists(select 1 from rel_match_contract where uid = :'carol'),
    'Can read accepted private profiles match-contract junctions'
);

select ok(
    not exists(select 1 from rel_match_contract where uid = :'dave'),
    'Cannot read pending private profiles match-contract junctions'
);

select ok(
    not exists(select 1 from rel_match_contract where uid = :'fred'),
    'Cannot read unfollowed private profiles match-contract junctions'
);
-- endregion:   select

-- region:      insert
select lives_ok(
    $$ insert into rel_match_contract (uid, contract, details) values ('$$ || :'alice' || $$', '$$ || :'contract2' || $$', '$$ || :'details1' || $$') $$,
    'Can insert own match-contract junctions'
);

select throws_ok(
    $$ insert into rel_match_contract (uid, contract, details) values ('$$ || :'bob' || $$', '$$ || :'contract1' || $$', '$$ || :'details6' || $$') $$,
    42501, -- RLS violation
    null,
    'Cannot insert match-contract junctions for other users'
);
-- endregion:   insert

-- region:      update
update rel_match_contract set details = :'details2' where uid = :'alice' and contract = :'contract2' and details = :'details5';
select ok(
    exists(select 1 from rel_match_contract where uid = :'alice' and contract = :'contract2' and details = :'details5'),
    'Cannot update own match-contract junctions'
);

update rel_match_contract set details = :'details6' where uid = :'bob' and contract = :'contract1' and details = :'details3';
select ok(
    exists(select 1 from rel_match_contract where uid = :'bob' and contract = :'contract1' and details = :'details3'),
    'Cannot update match-contract junctions on behalf of another user'
);
-- endregion:   update

-- region:      delete
delete from rel_match_contract where uid = :'alice' and contract = :'contract2' and details = :'details5';
select ok(
    not exists(select 1 from rel_match_contract where uid = :'alice' and contract = :'contract2' and details = :'details5'),
    'Can delete own match-contract junctions'
);

delete from rel_match_contract where uid = :'bob' and contract = :'contract1' and details = :'details3';
select ok(
    exists(select 1 from rel_match_contract where uid = :'bob' and contract = :'contract1' and details = :'details3'),
    'Cannot delete match-contract junctions on behalf of another user'
);
-- endregion:   delete

select * from finish();
rollback;