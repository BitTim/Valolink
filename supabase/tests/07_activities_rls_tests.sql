-- Tests for RLS policies on the activities table using seed data

\set alice          00000000-0000-0000-0000-000000000001
\set bob            00000000-0000-0000-0000-000000000002
\set carol          00000000-0000-0000-0000-000000000003
\set dave           00000000-0000-0000-0000-000000000004
\set erin           00000000-0000-0000-0000-000000000005
\set fred           00000000-0000-0000-0000-000000000006
\set grace          00000000-0000-0000-0000-000000000007
\set hans           00000000-0000-0000-0000-000000000008
\set nonexisting    00000000-0000-0000-0000-000000000099

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

begin;
select plan(15);

-- region:      anon

-- Impersonate anon role
set local role anon;

select is_empty(
    $$ select * from activities $$,
    'Anon cannot read any activities'
);

select throws_ok(
    $$ insert into activities (id, user_id) values ('$$ || :'activity1' || $$', '$$ || :'alice' || $$') $$,
    42501, -- RLS violation
    null,
    'Anon cannot insert any activity'
);

update activities set rr = 0 where id = :'activity1';
delete from activities where id = :'activity2';
set local role postgres;

select is(
    (select rr from activities where id = :'activity1'),
    350,
    'Anon cannot update any activity'
);

select ok(
    exists(select 1 from activities where id = :'activity2'),
    'Anon cannot delete any activity'
);

-- endregion:   anon
-- region:      alice

-- Impersonate Alice
set local role authenticated;
set local request.jwt.claims to '{"sub": "00000000-0000-0000-0000-000000000001"}';

-- region:      select
select ok(
    exists(select 1 from activities where user_id = :'alice'),
    'Can read own activity'
);

select ok(
    exists(select 1 from activities where user_id = :'bob'),
    'Can read public profile activity'
);

select ok(
    exists(select 1 from activities where user_id = :'carol'),
    'Can read accepted private profile activity'
);

select ok(
    not exists(select 1 from activities where user_id = :'dave'),
    'Cannot read pending private profile activity'
);

select ok(
    not exists(select 1 from activities where user_id = :'fred'),
    'Cannot read unfollowed private profile activity'
);
-- endregion:   select

-- region:      insert
select lives_ok(
    $$ insert into activities (id, user_id, type) values ('$$ || :'activity16' || $$', '$$ || :'alice' || $$', 'MATCH') $$,
    'Can insert activity for self'
);

select throws_ok(
    $$ insert into activities (id, user_id, type) values ('$$ || :'activity16' || $$', '$$ || :'bob' || $$', 'MATCH') $$,
    42501, -- RLS violation
    null,
    'Cannot insert activity for other user'
);
-- endregion:   insert

-- region:      update
update activities set rr = 0 where id = :'activity1' and user_id = :'alice';
select is(
    (select rr from activities where id = :'activity1' and user_id = :'alice'),
    0,
    'Can update own activity'
);

update activities set rr = 0 where id = :'activity6' and user_id = :'bob';
select is(
    (select rr from activities where id = :'activity6' and user_id = :'bob'),
    350,
    'Cannot update other user activity'
);
-- endregion:   update

-- region:      delete
delete from activities where id = :'activity1' and user_id = :'alice';
select ok(
    not exists(select 1 from activities where id = :'activity1' and user_id = :'alice'),
    'Can delete own activity'
);

delete from activities where id = :'activity6' and user_id = :'bob';
select ok(
    exists(select 1 from activities where id = :'activity6' and user_id = :'bob'),
    'Cannot delete other user activity'
);
-- endregion:   delete