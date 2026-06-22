-- Tests for RLS policies on the users table using seed data

\set alice          00000000-0000-0000-0000-000000000001
\set bob            00000000-0000-0000-0000-000000000002
\set carol          00000000-0000-0000-0000-000000000003
\set dave           00000000-0000-0000-0000-000000000004
\set erin           00000000-0000-0000-0000-000000000005
\set fred           00000000-0000-0000-0000-000000000006
\set grace          00000000-0000-0000-0000-000000000007
\set hans           00000000-0000-0000-0000-000000000008
\set nonexisting    00000000-0000-0000-0000-000000000099

begin;
select plan(12);

-- region:      anon
-- Impersonate anon role
set local role anon;

select is_empty(
    $$ select * from users $$,
    'Anon cannot read any profile'
);

select throws_ok(
    $$ insert into users (id, username) values ('$$  || :'nonexisting' || $$' , 'Test') $$,
    42501, -- RLS violation
    null,
    'Anon cannot insert a profile'
);

update users set username = 'Anon Updated' where id = :'alice';
delete from users where id = :'bob';
set local role postgres;

select is(
    (select username from users where id = :'alice'),
    'Alice',
    'Anon cannot update any profile'
);

select ok(
    exists(select 1 from users where id = :'bob'),
    'Anon cannot delete any profile'
);
--- endregion:  anon

-- region:      authenticated
-- Impersonate Alice
set local role authenticated;
set local request.jwt.claims to '{"sub": "00000000-0000-0000-0000-000000000001"}';

-- region:      select
select ok(
    exists(select 1 from users where id = :'alice'),
    'Can read own profile'
);

select ok(
    exists(select 1 from users where id = :'bob'),
    'Can read any other public profile'
);

select ok(
    exists(select 1 from users where id = :'dave'),
    'Can read any other private profile'
);
-- endregion:   select

-- region:      insert
select throws_ok(
    $$ insert into users (id, username) values ('$$  || :'nonexisting' || $$', 'Test') $$,
    42501, -- RLS violation
    null,
    'Cannot insert a profile on behalf of another user'
);
-- endregion:   insert

-- region:      update
update users set username = 'Alice Updated' where id = :'alice';
select is(
    (select username from users where id = :'alice'),
    'Alice Updated',
    'Can update own profile'
);

update users set username = 'Bob Updated' where id = :'bob';
select is(
    (select username from users where id = :'bob'),
    'Bob',
    'Cannot update another users profile'
);
-- endregion:   update

-- region:      delete
delete from users where id = :'alice';
select ok(
    exists(select 1 from users where id = :'alice'),
    'Cannot delete own profile'
);

delete from users where id = :'bob';
select ok(
    exists(select 1 from users where id = :'bob'),
    'Cannot delete another users profile'
);
-- endregion:   delete
-- endregion:   authenticated

select * from finish();
rollback;