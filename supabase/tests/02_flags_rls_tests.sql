-- Tests for RLS policies on the flags table using seed data

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
select plan(14);

-- region:      anon
-- Impersonate anon role
set local role anon;

select is_empty(
    $$ select * from flags $$,
    'Anon cannot read any flags'
);

select throws_ok(
    $$ insert into flags (user_id) values ('$$ || :'nonexisting' || $$') $$,
    42501, -- RLS violation
    null,
    'Anon cannot insert flags'
);

update flags set has_onboarded = true where user_id = :'dave';
delete from flags where user_id = :'bob';
set local role postgres;

select is(
    (select has_onboarded from flags where user_id = :'dave'),
    false,
    'Anon cannot update any flags'
);

select ok(
    exists(select 1 from flags where user_id = :'bob'),
    'Anon cannot delete any flags'
);
-- endregion:  anon

-- region:      authenticated
-- Impersonate Alice
set local role authenticated;
set local request.jwt.claims to '{"sub": "00000000-0000-0000-0000-000000000001"}';

-- region:      select
select ok(
    exists(select 1 from flags where user_id = :'alice'),
    'Can read own flags'
);

select ok(
    exists(select 1 from flags where user_id = :'bob'),
    'Can read a public profiles flags'
);

select ok(
    exists(select 1 from flags where user_id = :'carol'),
    'Can read an accepted private profiles flags'
);

select ok(
    not exists(select 1 from flags where user_id = :'dave'),
    'Cannot read a pending private profiles flags'
);

select ok(
    not exists(select 1 from flags where user_id = :'fred'),
    'Cannot read an unfollowed private profiles flags'
);
-- endregion:   select

-- region:      insert
select throws_ok(
    $$ insert into flags (user_id) values ('$$ || :'nonexisting' || $$') $$,
    42501, -- RLS violation
    null,
    'Cannot insert flags on behalf of another user'
);
-- endregion:   insert

-- region:      update
update flags set has_onboarded = false where user_id = :'alice';
select is(
    (select has_onboarded from flags where user_id = :'alice'),
    false,
    'Can update own flags'
);

update flags set has_onboarded = false where user_id = :'bob';
select is(
    (select has_onboarded from flags where user_id = :'bob'),
    true,
    'Cannot update another users flags'
);
-- endregion:   update

-- region:      delete
delete from flags where user_id = :'alice';
select ok(
    exists(select 1 from flags where user_id = :'alice'),
    'Cannot delete own flags'
);

delete from flags where user_id = :'bob';
select ok(
    exists(select 1 from flags where user_id = :'bob'),
    'Cannot delete another users flags'
);
-- endregion:   authenticated

select * from finish();
rollback;