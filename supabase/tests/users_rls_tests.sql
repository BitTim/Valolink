-- Tests for RLS policies on the users table using seed data

\set alice          00000000-0000-0000-0000-000000000001
\set bob            00000000-0000-0000-0000-000000000002
\set carol          00000000-0000-0000-0000-000000000003
\set dave           00000000-0000-0000-0000-000000000004
\set erin           00000000-0000-0000-0000-000000000005
\set nonexisting    00000000-0000-0000-0000-000000000099

begin;
select plan(12);

-- region:      anon
-- Impersonate anon role
set local role anon;

select is_empty(
    $$ select * from users $$,
    'Anon cannot see any profile'
);

select throws_ok(
    $$ insert into users (id, username) values ('$$  || :'nonexisting' || $$' , 'Test') $$,
    42501, -- RLS violation
    null,
    'Anon cannot insert a user profile'
);

update users set username = 'Anon Updated' where id = :'alice';
delete from users where id = :'bob';
set local role postgres;

select is(
    (select username from users where id = :'alice'),
    'Alice',
    'Anon cannot update user profiles'
);

select ok(
    exists(select 1 from users where id = :'bob'),
    'Anon cannot delete user profiles'
);
--- endregion:  anon

-- region:      authenticated
-- Impersonate Alice
set local role authenticated;
set local request.jwt.claims to '{"sub": "00000000-0000-0000-0000-000000000001"}';

-- region:      select
select ok(
    exists(select 1 from users where id = :'alice'),
    'User can see own profile'
);

select ok(
    exists(select 1 from users where id = :'bob'),
    'User can see other public profile'
);

select ok(
    exists(select 1 from users where id = :'dave'),
    'User can see other private profile'
);
-- endregion:   select

-- region:      insert
select throws_ok(
    $$ insert into users (id, username) values ('$$  || :'nonexisting' || $$', 'Test') $$,
    42501, -- RLS violation
    null,
    'User cannot insert a profile for another user'
);
-- endregion:   insert

-- region:      update
update users set username = 'Alice Updated' where id = :'alice';
select is(
    (select username from users where id = :'alice'),
    'Alice Updated',
    'User can update own profile'
);

update users set username = 'Bob Updated' where id = :'bob';
select is(
    (select username from users where id = :'bob'),
    'Bob',
    'User cannot update other profiles'
);
-- endregion:   update

-- region:      delete
delete from users where id = :'alice';
select ok(
    exists(select 1 from users where id = :'alice'),
    'User cannot delete own profile'
);

delete from users where id = :'bob';
select ok(
    exists(select 1 from users where id = :'bob'),
    'User cannot delete other profiles'
);
-- endregion:   delete
-- endregion:   authenticated

select * from finish();
rollback;