-- Tests triggers with seed data

\set alice          00000000-0000-0000-0000-000000000001
\set bob            00000000-0000-0000-0000-000000000002
\set carol          00000000-0000-0000-0000-000000000003
\set dave           00000000-0000-0000-0000-000000000004
\set erin           00000000-0000-0000-0000-000000000005
\set fred           00000000-0000-0000-0000-000000000006
\set grace          00000000-0000-0000-0000-000000000007
\set hans           00000000-0000-0000-0000-000000000008
\set new            00000000-0000-0000-0000-000000000009
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

\set activity18     fff00000-0000-0000-0000-000000000012
\set activity19     fff00000-0000-0000-0000-000000000013
\set activity20     fff00000-0000-0000-0000-000000000014
\set activity21     fff00000-0000-0000-0000-000000000015

\set match1       ccc00000-0000-0000-0000-000000000001
\set match2       ccc00000-0000-0000-0000-000000000002
\set match3       ccc00000-0000-0000-0000-000000000003
\set match4       ccc00000-0000-0000-0000-000000000004
\set match5       ccc00000-0000-0000-0000-000000000005
\set match6       ccc00000-0000-0000-0000-000000000006
\set match7       ccc00000-0000-0000-0000-000000000007

\set map1           ddd00000-0000-0000-0000-000000000001
\set map2           ddd00000-0000-0000-0000-000000000002
\set map3           ddd00000-0000-0000-0000-000000000003

\set mode1          eee00000-0000-0000-0000-000000000001
\set mode2          eee00000-0000-0000-0000-000000000002

begin;
select plan(18);

select ok(
    exists(select 1 from add_test_user(
        'new@example.com',
        :'new',
        'test123',
        'New User'
    )),
    'add_test_user function created a new user'
);

select ok(
    exists(select 1 from users where id = :'new'),
    'Trigger created a new user when auth user was created'
);

select ok(
    exists(select 1 from flags where user_id = :'new'),
    'Trigger created flags for the new user'
);

update users set is_private = true where id = :'new';

select ok(
    (select updated_at >= created_at from users where id = :'new'),
    'Trigger updated the updated_at column on users when is_private was updated'
);

insert into follows (follower, following) values (:'alice', :'new');

select is(
    (select relation_status from follows where follower = :'alice' and following = :'new'),
    'PENDING',
    'Trigger did not accept the follow since the profile is private'
);

insert into follows (follower, following) values (:'new', :'bob');

select is(
    (select relation_status from follows where follower = :'new' and following = :'bob'),
    'ACCEPTED',
    'Trigger accepted the follow since the profile is public'
);

update users set is_private = false where id = :'new';

select is(
    (select relation_status from follows where follower = :'alice' and following = :'new'),
    'ACCEPTED',
    'Trigger accepted the pending follow when profile was set to public'
);

set constraints match_participants_match_fkey deferred;
insert into activities (id, user_id, type) values (:'activity18', :'new', 'MATCH');
insert into match_participants (user_id, match, activity, is_owner) values (:'new', :'match7', :'activity18', true);

insert into activities (id, user_id, type) values (:'activity19', :'bob', 'MATCH');
insert into match_participants (user_id, match, activity) values (:'bob', :'match7', :'activity19');

insert into activities (id, user_id, type) values (:'activity20', :'carol', 'MATCH');
insert into match_participants (user_id, match, activity) values (:'carol', :'match7', :'activity20');

insert into activities (id, user_id, type) values (:'activity21', :'alice', 'MATCH');
insert into match_participants (user_id, match, activity) values (:'alice', :'match7', :'activity21');

insert into matches (id, map, mode) values (:'match7', :'map1', :'mode1');

delete from match_participants where match = :'match7' and user_id = :'new';
select ok(
    not exists(select 1 from match_participants where match = :'match7' and user_id = :'new'),
    'Original owner deleted a match'
);

select ok(
    exists(select 1 from matches where id = :'match7'),
    'Trigger did not delete match match since there is still an existing match referencing it'
);

select is(
    (select is_owner from match_participants where match = :'match7' and user_id = :'bob'),
    true,
    'Trigger made Bob the owner of the match since it is the oldest match referencing the match after the original owner was deleted'
);

delete from match_participants where match = :'match7' and user_id = :'carol';
select ok(
    not exists(select 1 from match_participants where match = :'match7' and user_id = :'carol'),
    'Carol deleted a match'
);

select ok(
    exists(select 1 from matches where id = :'match7'),
    'Trigger did not delete match match since there is still an existing match referencing it'
);

select is(
    (select is_owner from match_participants where match = :'match7' and user_id = :'bob'),
    true,
    'Bob is still the owner of the match after Carol deleted her match'
);

delete from match_participants where match = :'match7' and user_id = :'bob';
select ok(
    not exists(select 1 from match_participants where match = :'match7' and user_id = :'new'),
    'Bob deleted a match'
);

select ok(
    exists(select 1 from matches where id = :'match7'),
    'Trigger did not delete match match since there is still an existing match referencing it'
);

select is(
    (select is_owner from match_participants where match = :'match7' and user_id = :'alice'),
    true,
    'Trigger made Alice the owner of the match since bob deleted his match and Alice is now the oldest match referencing the match'
);

delete from match_participants where match = :'match7' and user_id = :'alice';
select ok(
    not exists(select 1 from match_participants where match = :'match7'),
    'Alice deleted the last match referencing the match'
);

select ok(
    not exists(select 1 from matches where id = :'match7'),
    'Trigger deleted match match since there are no match_participants referencing it'
);