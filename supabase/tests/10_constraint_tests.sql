-- Tests constraints on all tables with seed data

\set alice          00000000-0000-0000-0000-000000000001
\set bob            00000000-0000-0000-0000-000000000002
\set carol          00000000-0000-0000-0000-000000000003
\set dave           00000000-0000-0000-0000-000000000004
\set erin           00000000-0000-0000-0000-000000000005
\set fred           00000000-0000-0000-0000-000000000006
\set grace          00000000-0000-0000-0000-000000000007
\set hans           00000000-0000-0000-0000-000000000008
\set nonexisting    00000000-0000-0000-0000-000000000099

\set jett           a3bfb853-43b2-7238-a4f1-ad90e9e46bcc
\set sage           569fdd95-4d10-43ab-ca70-79becc718b46
\set phoenix        eb93336a-449b-9c1e-0a7d-5bae39493506
\set reyna          f94c3b30-42be-e959-889c-5aa313dba261

\set contract1      aaa00000-0000-0000-0000-000000000001
\set contract2      aaa00000-0000-0000-0000-000000000002
\set contract3      aaa00000-0000-0000-0000-000000000003

\set level1         bbb00000-0000-0000-0000-000000000001
\set level2         bbb00000-0000-0000-0000-000000000002
\set level3         bbb00000-0000-0000-0000-000000000003
\set level4         bbb00000-0000-0000-0000-000000000004

\set details1       ccc00000-0000-0000-0000-000000000001
\set details2       ccc00000-0000-0000-0000-000000000002
\set details3       ccc00000-0000-0000-0000-000000000003
\set details4       ccc00000-0000-0000-0000-000000000004
\set details5       ccc00000-0000-0000-0000-000000000005
\set details6       ccc00000-0000-0000-0000-000000000006

begin;
select plan(22);

-- region:      users
select throws_ok(
    $$ insert into users (id) values ('$$ || :'alice' || $$') $$,
    23505, -- unique violation
    null,
    'Cannot insert a duplicate profile'
);

select throws_ok(
    $$ insert into users (id) values ('$$ || :'nonexisting' || $$') $$,
    23503, -- foreign key violation
    null,
    'uid must reference a valid auth.users entry'
);
-- endregion:   users

-- region:      flags
select throws_ok(
    $$ insert into flags (uid) values ('$$ || :'alice' || $$') $$,
    23505, -- unique violation
    null,
    'Cannot insert duplicate flags for a user'
);

select throws_ok(
    $$ insert into flags (uid) values ('$$ || :'nonexisting' || $$') $$,
    23503, -- foreign key violation
    null,
    'uid in flags must reference a valid users entry'
);
-- endregion:   flags

-- region:      follows
select throws_ok(
    $$ insert into follows (follower, following) values ('$$ || :'alice' || $$', '$$ || :'bob' || $$') $$,
    23505, -- unique violation
    null,
    'Cannot follow the same profile twice'
);

select throws_ok(
    $$ insert into follows (follower, following) values ('$$ || :'alice' || $$', '$$ || :'alice' || $$') $$,
    23514, -- check violation
    null,
    'Cannot follow self'
);

select throws_ok(
    $$ insert into follows (follower, following) values ('$$ || :'nonexisting' || $$', '$$ || :'alice' || $$') $$,
    23503, -- foreign key violation
    null,
    'follower must reference a valid users entry'
);

select throws_ok(
    $$ insert into follows (follower, following) values ('$$ || :'alice' || $$', '$$ || :'nonexisting' || $$') $$,
    23503, -- foreign key violation
    null,
    'following must reference a valid users entry'
);
-- endregion:   follows

-- region:      agents
select throws_ok(
    $$ insert into agents (uid, agent) values ('$$ || :'alice' || $$', '$$ || :'jett' || $$') $$,
    23505, -- unique violation
    null,
    'Cannot insert duplicate agent'
);

select throws_ok(
    $$ insert into agents (uid, agent) values ('$$ || :'nonexisting' || $$', '$$ || :'jett' || $$') $$,
    23503, -- foreign key violation
    null,
    'uid in agents must reference a valid users entry'
);
-- endregion:   agents

-- region:      contracts
select throws_ok(
    $$ insert into contracts (uid, contract) values ('$$ || :'alice' || $$', '$$ || :'contract1' || $$') $$,
    23505, -- unique violation
    null,
    'Cannot insert duplicate contract'
);

select throws_ok(
    $$ insert into contracts (uid, contract) values ('$$ || :'nonexisting' || $$', '$$ || :'contract1' || $$') $$,
    23503, -- foreign key violation
    null,
    'uid in contracts must reference a valid users entry'
);
-- endregion:   contracts

-- region:      levels
select throws_ok(
    $$ insert into levels (uid, contract, level) values ('$$ || :'alice' || $$', '$$ || :'contract1' || $$', '$$ || :'level1' || $$') $$,
    23505, -- unique violation
    null,
    'Cannot insert duplicate level'
);

select throws_ok(
    $$ insert into levels (uid, contract, level) values ('$$ || :'nonexisting' || $$', '$$ || :'contract1' || $$', '$$ || :'level1' || $$') $$,
    23503, -- foreign key violation
    null,
    'uid in levels must reference a valid users entry'
);
-- endregion:   levels

-- region:      matches
select throws_ok(
    $$ insert into matches (uid, details) values ('$$ || :'alice' || $$', '$$ || :'details1' || $$') $$,
    23505, -- unique violation
    null,
    'Cannot insert duplicate match'
);

select throws_ok(
    $$ insert into matches (uid, details) values ('$$ || :'nonexisting' || $$', '$$ || :'details1' || $$') $$,
    23503, -- foreign key violation
    null,
    'uid in matches must reference a valid users entry'
);

select throws_ok(
    $$ insert into matches (uid, details) values ('$$ || :'alice' || $$', '$$ || :'nonexisting' || $$') $$,
    23503, -- foreign key violation
    null,
    'details in matches must reference a valid match_details entry'
);

select throws_ok(
    $$ insert into matches (uid, details, is_owner) values ('$$ || :'bob' || $$', '$$ || :'details1' || $$', true) $$,
    23505, -- unique violation
    null,
    'detail cannot be assigned more than one owner'
);
-- endregion:   matches

-- region:      rel_match_contract
select throws_ok(
    $$ insert into rel_match_contract (uid, contract, details) values ('$$ || :'alice' || $$', '$$ || :'contract1' || $$', '$$ || :'details1' || $$') $$,
    23505, -- unique violation
    null,
    'Cannot insert duplicate match-contract relation'
);

select throws_ok(
    $$ insert into rel_match_contract (uid, contract, details) values ('$$ || :'nonexisting' || $$', '$$ || :'contract1' || $$', '$$ || :'details1' || $$') $$,
    23503, -- foreign key violation
    null,
    'uid in rel_match_contract must reference a valid users entry'
);

select throws_ok(
    $$ insert into rel_match_contract (uid, contract, details) values ('$$ || :'alice' || $$', '$$ || :'nonexisting' || $$', '$$ || :'details1' || $$') $$,
    23503, -- foreign key violation
    null,
    'contract in rel_match_contract must reference a valid contracts entry'
);

select throws_ok(
    $$ insert into rel_match_contract (uid, contract, details) values ('$$ || :'alice' || $$', '$$ || :'contract1' || $$', '$$ || :'nonexisting' || $$') $$,
    23503, -- foreign key violation
    null,
    'details in rel_match_contract must reference a valid match_details entry'
);
-- endregion:   rel_match_contract

select * from finish();
rollback;