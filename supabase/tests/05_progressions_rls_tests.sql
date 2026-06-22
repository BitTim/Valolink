-- Tests for RLS policies on the progressions table using seed data

\set alice          00000000-0000-0000-0000-000000000001
\set bob            00000000-0000-0000-0000-000000000002
\set carol          00000000-0000-0000-0000-000000000003
\set dave           00000000-0000-0000-0000-000000000004
\set erin           00000000-0000-0000-0000-000000000005
\set fred           00000000-0000-0000-0000-000000000006
\set grace          00000000-0000-0000-0000-000000000007
\set hans           00000000-0000-0000-0000-000000000008
\set nonexisting    00000000-0000-0000-0000-000000000099

\set progression1      aaa00000-0000-0000-0000-000000000001
\set progression2      aaa00000-0000-0000-0000-000000000002
\set progression3      aaa00000-0000-0000-0000-000000000003

begin;
select plan(15);

-- region:      anon
-- Impersonate anon role
set local role anon;

select is_empty(
    $$ select * from progressions $$,
    'Anon cannot read any follows'
);

select throws_ok(
    $$ insert into progressions (user_id, progression) values ('$$ || :'alice' || $$', '$$ || :'progression3' || $$') $$,
    42501, -- RLS violation
    null,
    'Anon cannot insert progressions'
);

update progressions set free_only = true where user_id = :'alice' and progression = :'progression1';
delete from progressions where user_id = :'alice' and progression = :'progression2';
set local role postgres;

select is(
    (select free_only from progressions where user_id = :'alice' and progression = :'progression1'),
    false,
    'Anon cannot update any progressions'
);

select ok(
    exists(select 1 from progressions where user_id = :'alice' and progression = :'progression2'),
    'Anon cannot delete any progressions'
);
-- endregion:   anon

-- region:      authenticated
-- Impersonate Alice
set local role authenticated;
set local request.jwt.claims to '{"sub": "00000000-0000-0000-0000-000000000001"}';

-- region:      select
select ok(
    exists(select 1 from progressions where user_id = :'alice'),
    'Can read own progressions'
);

select ok(
    exists(select 1 from progressions where user_id = :'erin'),
    'Can read public profiles progressions'
);

select ok(
    exists(select 1 from progressions where user_id = :'carol'),
    'Can read accepted private profiles progressions'
);

select ok(
    not exists(select 1 from progressions where user_id = :'dave'),
    'Cannot read pending private profiles progressions'
);

select ok(
    not exists(select 1 from progressions where user_id = :'fred'),
    'Cannot read unfollowed profiles progressions'
);
-- endregion:   select

-- region:      insert
select lives_ok(
    $$ insert into progressions (user_id, progression) values ('$$ || :'alice' || $$', '$$ || :'progression3' || $$') $$,
    'Can insert own progressions'
);

select throws_ok(
    $$ insert into progressions (user_id, progression) values ('$$ || :'bob' || $$', '$$ || :'progression3' || $$') $$,
    42501, -- RLS violation
    null,
    'Cannot insert progressions on behalf of another user'
);
-- endregion:   insert

-- region:      update
update progressions set free_only = true where user_id = :'alice' and progression = :'progression1';
select is(
    (select free_only from progressions where user_id = :'alice' and progression = :'progression1'),
    true,
    'Can update own progressions'
);

update progressions set free_only = true where user_id = :'bob' and progression = :'progression1';
select is(
    (select free_only from progressions where user_id = :'bob' and progression = :'progression1'),
    false,
    'Cannot update progressions on behalf of another user'
);
-- endregion:   update

-- region:      delete
delete from progressions where user_id = :'alice' and progression = :'progression2';
select ok(
    not exists(select 1 from progressions where user_id = :'alice' and progression = :'progression2'),
    'Can delete own progressions'
);

delete from progressions where user_id = :'bob' and progression = :'progression1';
select ok(
    exists(select 1 from progressions where user_id = :'bob' and progression = :'progression1'),
    'Cannot delete progressions on behalf of another user'
);
-- endregion:   delete
-- endregion:   authenticated

select * from finish();
rollback;