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

\set progression1      aaa00000-0000-0000-0000-000000000001
\set progression2      aaa00000-0000-0000-0000-000000000002
\set progression3      aaa00000-0000-0000-0000-000000000003

\set level1         bbb00000-0000-0000-0000-000000000001
\set level2         bbb00000-0000-0000-0000-000000000002
\set level3         bbb00000-0000-0000-0000-000000000003
\set level4         bbb00000-0000-0000-0000-000000000004

\set activity1      fff00000-0000-0000-0000-000000000001
\set activity2      fff00000-0000-0000-0000-000000000002
\set activity3      fff00000-0000-0000-0000-000000000003
\set activity4      fff00000-0000-0000-0000-000000000004
\set activity5      fff00000-0000-0000-0000-000000000005
\set activity6      fff00000-0000-0000-0000-000000000006
\set activity7      fff00000-0000-0000-0000-000000000007
\set activity8      fff00000-0000-0000-0000-000000000008
\set activity9      fff00000-0000-0000-0000-000000000009
\set activity10     fff00000-0000-0000-0000-00000000000a
\set activity11     fff00000-0000-0000-0000-00000000000b
\set activity12     fff00000-0000-0000-0000-00000000000c
\set activity13     fff00000-0000-0000-0000-00000000000d
\set activity14     fff00000-0000-0000-0000-00000000000e
\set activity15     fff00000-0000-0000-0000-00000000000f
\set activity16     fff00000-0000-0000-0000-000000000010
\set activity17     fff00000-0000-0000-0000-000000000011
\set activity18     fff00000-0000-0000-0000-000000000012
\set activity19     fff00000-0000-0000-0000-000000000013

\set match1       ccc00000-0000-0000-0000-000000000001
\set match2       ccc00000-0000-0000-0000-000000000002
\set match3       ccc00000-0000-0000-0000-000000000003
\set match4       ccc00000-0000-0000-0000-000000000004
\set match5       ccc00000-0000-0000-0000-000000000005
\set match6       ccc00000-0000-0000-0000-000000000006

\set map1           ddd00000-0000-0000-0000-000000000001
\set map2           ddd00000-0000-0000-0000-000000000002
\set map3           ddd00000-0000-0000-0000-000000000003

\set mode1          eee00000-0000-0000-0000-000000000001
\set mode2          eee00000-0000-0000-0000-000000000002

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
    'user_id must reference a valid auth.users entry'
);
-- endregion:   users

-- region:      flags
select throws_ok(
    $$ insert into flags (user_id) values ('$$ || :'alice' || $$') $$,
    23505, -- unique violation
    null,
    'Cannot insert duplicate flags for a user'
);

select throws_ok(
    $$ insert into flags (user_id) values ('$$ || :'nonexisting' || $$') $$,
    23503, -- foreign key violation
    null,
    'user_id in flags must reference a valid users entry'
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
    $$ insert into agents (user_id, agent) values ('$$ || :'alice' || $$', '$$ || :'jett' || $$') $$,
    23505, -- unique violation
    null,
    'Cannot insert duplicate agent'
);

select throws_ok(
    $$ insert into agents (user_id, agent) values ('$$ || :'nonexisting' || $$', '$$ || :'jett' || $$') $$,
    23503, -- foreign key violation
    null,
    'user_id in agents must reference a valid users entry'
);
-- endregion:   agents

-- region:      progressions
select throws_ok(
    $$ insert into progressions (user_id, progression) values ('$$ || :'alice' || $$', '$$ || :'progression1' || $$') $$,
    23505, -- unique violation
    null,
    'Cannot insert duplicate progression'
);

select throws_ok(
    $$ insert into progressions (user_id, progression) values ('$$ || :'nonexisting' || $$', '$$ || :'progression1' || $$') $$,
    23503, -- foreign key violation
    null,
    'user_id in progressions must reference a valid users entry'
);
-- endregion:   progressions

-- region:      purchased_levels
select throws_ok(
    $$ insert into purchased_levels (user_id, progression, level) values ('$$ || :'alice' || $$', '$$ || :'progression1' || $$', '$$ || :'level1' || $$') $$,
    23505, -- unique violation
    null,
    'Cannot insert duplicate level'
);

select throws_ok(
    $$ insert into purchased_levels (user_id, progression, level) values ('$$ || :'nonexisting' || $$', '$$ || :'progression1' || $$', '$$ || :'level1' || $$') $$,
    23503, -- foreign key violation
    null,
    'user_id in purchased_levels must reference a valid users entry'
);
-- endregion:   purchased_levels

-- region:      activities
select throws_ok(
    $$ insert into activities (id, user_id, type) values ('$$ || :'activity2' || $$', '$$ || :'alice' || $$', 'MATCH') $$,
    23505, -- unique violation
    null,
    'Cannot insert duplicate activity'
);

select throws_ok(
    $$ insert into activities (id, user_id, type) values ('$$ || :'activity17' || $$', '$$ || :'nonexisting' || $$', 'MATCH') $$,
    23503, -- foreign key violation
    null,
    'user_id in activities must reference a valid users entry'
);

-- endregion:   activities

-- region:      match_participants
select throws_ok(
    $$ insert into match_participants (user_id, match, activity) values ('$$ || :'alice' || $$', '$$ || :'match1' || $$', '$$ || :'activity2' || $$') $$,
    23505, -- unique violation
    null,
    'Cannot insert duplicate match participant'
);

select throws_ok(
    $$ insert into activities (id, user_id, type) values ('$$ || :'activity18' || $$', '$$ || :'alice' || $$', 'MATCH');
    insert into match_participants (user_id, match, activity) values ('$$ || :'nonexisting' || $$', '$$ || :'match1' || $$', '$$ || :'activity18' || $$') $$,
    23503, -- foreign key violation
    null,
    'user_id in match_participants must reference a valid users entry'
);

select throws_ok(
    $$ insert into activities (id, user_id, type) values ('$$ || :'activity19' || $$', '$$ || :'alice' || $$', 'MATCH');
    insert into match_participants (user_id, match, activity) values ('$$ || :'alice' || $$', '$$ || :'nonexisting' || $$', '$$ || :'activity19' || $$') $$,
    23503, -- foreign key violation
    null,
    'match in match_participants must reference a valid match_match entry'
);

select throws_ok(
    $$ insert into match_participants (user_id, match, activity) values ('$$ || :'alice' || $$', '$$ || :'match1' || $$', '$$ || :'nonexisting' || $$') $$,
    23503, -- foreign key violation
    null,
    'activity in match_participants must reference a valid activity entry'
);

select throws_ok(
    $$ insert into match_participants (user_id, match, activity, is_owner) values ('$$ || :'bob' || $$', '$$ || :'match1' || $$', '$$ || :'activity2' || $$', true) $$,
    23505, -- unique violation
    null,
    'match cannot be assigned more than one owner'
);
-- endregion:   match_participants

-- region:      matches
select throws_ok(
    $$ insert into matches (id, map, mode) values ('$$ || :'match1' || $$', '$$ || :'map1' || $$', '$$ || :'mode1' || $$') $$,
    23505, -- unique violation
    null,
    'Cannot insert duplicate match'
);
-- endregion:   matches

select * from finish();
rollback;