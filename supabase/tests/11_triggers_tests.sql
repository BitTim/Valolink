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
\set details7       ccc00000-0000-0000-0000-000000000007

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
    exists(select 1 from flags where uid = :'new'),
    'Trigger created flags for the new user'
);

update users set is_private = true where id = :'new';

select ok(
    (select updated_at >= created_at from users where id = :'new'),
    'Trigger updated the updated_at column on users when is_private was updated'
);

insert into follows (follower, following) values (:'alice', :'new');

select is(
    (select accepted from follows where follower = :'alice' and following = :'new'),
    false,
    'Trigger did not accept the follow since the profile is private'
);

insert into follows (follower, following) values (:'new', :'bob');

select ok(
    (select accepted from follows where follower = :'new' and following = :'bob'),
    'Trigger accepted the follow since the profile is public'
);

update users set is_private = false where id = :'new';

select ok(
    (select accepted from follows where follower = :'alice' and following = :'new'),
    'Trigger accepted the pending follow when profile was set to public'
);

set constraints matches_details_fkey deferred;
insert into matches (uid, details, is_owner) values (:'new', :'details7', true);
insert into matches (uid, details) values (:'bob', :'details7');
insert into matches (uid, details) values (:'carol', :'details7');
insert into matches (uid, details) values (:'alice', :'details7');
insert into match_details (id, map, mode) values (:'details7', :'map1', :'mode1');

delete from matches where details = :'details7' and uid = :'new';
select ok(
    not exists(select 1 from matches where details = :'details7' and uid = :'new'),
    'Original owner deleted a match'
);

select ok(
    exists(select 1 from match_details where id = :'details7'),
    'Trigger did not delete match details since there is still an existing match referencing it'
);

select is(
    (select is_owner from matches where details = :'details7' and uid = :'bob'),
    true,
    'Trigger made Bob the owner of the match since it is the oldest match referencing the details after the original owner was deleted'
);

delete from matches where details = :'details7' and uid = :'carol';
select ok(
    not exists(select 1 from matches where details = :'details7' and uid = :'carol'),
    'Carol deleted a match'
);

select ok(
    exists(select 1 from match_details where id = :'details7'),
    'Trigger did not delete match details since there is still an existing match referencing it'
);

select is(
    (select is_owner from matches where details = :'details7' and uid = :'bob'),
    true,
    'Bob is still the owner of the match after Carol deleted her match'
);

delete from matches where details = :'details7' and uid = :'bob';
select ok(
    not exists(select 1 from matches where details = :'details7' and uid = :'new'),
    'Bob deleted a match'
);

select ok(
    exists(select 1 from match_details where id = :'details7'),
    'Trigger did not delete match details since there is still an existing match referencing it'
);

select is(
    (select is_owner from matches where details = :'details7' and uid = :'alice'),
    true,
    'Trigger made Alice the owner of the match since bob deleted his match and Alice is now the oldest match referencing the details'
);

delete from matches where details = :'details7' and uid = :'alice';
select ok(
    not exists(select 1 from matches where details = :'details7'),
    'Alice deleted the last match referencing the details'
);

select ok(
    not exists(select 1 from match_details where id = :'details7'),
    'Trigger deleted match details since there are no matches referencing it'
);