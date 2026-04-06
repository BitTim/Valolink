-- Tests for RLS policies on the agents table using seed data

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

begin;
select plan(15);

-- region:      anon
-- Impersonate anon role
set local role anon;

select is_empty(
    $$ select * from agents $$,
    'Anon cannot read any agents'
);

select throws_ok(
    $$ insert into agents (uid, agent) values ('$$ || :'fred' || $$', '$$ || :'jett' || $$') $$,
    42501, -- RLS violation
    null,
    'Anon cannot insert agents'
);

update agents set agent = :'jett' where uid = :'alice' and agent = :'sage';
delete from agents where uid = :'alice' and agent = :'jett';
set local role postgres;

select is(
    (select agent from agents where uid = :'alice' and agent = :'sage'),
    :'sage',
    'Anon cannot update any agents'
);

select ok(
    exists(select 1 from agents where uid = :'alice' and agent = :'jett'),
    'Anon cannot delete any agents'
);
-- endregion:   anon

-- region:      authenticated
-- Impersonate Alice
set local role authenticated;
set local request.jwt.claims to '{"sub": "00000000-0000-0000-0000-000000000001"}';

-- region:      select
select ok(
    exists(select 1 from agents where uid = :'alice'),
    'Can read own agents'
);

select ok(
    exists(select 1 from agents where uid = :'bob'),
    'Can read public profiles agents'
);

select ok(
    exists(select 1 from agents where uid = :'carol'),
    'Can read accepted private profiles agents'
);

select ok(
    not exists(select 1 from agents where uid = :'dave'),
    'Cannot read pending private profiles agents'
);

select ok(
    not exists(select 1 from agents where uid = :'fred'),
    'Cannot read unfollowed private profiles agents'
);
-- endregion:   select

-- region:      insert
select lives_ok(
    $$ insert into agents (uid, agent) values ('$$ || :'alice' || $$', '$$ || :'phoenix' || $$') $$,
    'Can insert own agents'
);

select throws_ok(
    $$ insert into agents (uid, agent) values ('$$ || :'bob' || $$', '$$ || :'jett' || $$') $$,
    42501, -- RLS violation
    null,
    'Cannot insert agents for other users'
);
-- endregion:   insert

-- region:      update
update agents set agent = :'reyna' where uid = :'alice' and agent = :'jett';
select ok(
    not exists(select 1 from agents where uid = :'alice' and agent = :'reyna'),
    'Cannot update own agents'
);

update agents set agent = :'reyna' where uid = :'bob' and agent = :'phoenix';
select ok(
    not exists(select 1 from agents where uid = :'bob' and agent = :'reyna'),
    'Cannot update agents on behalf of another user'
);
-- endregion:   update

-- region:      delete
delete from agents where uid = :'alice' and agent = :'sage';
select ok(
    not exists(select 1 from agents where uid = :'alice' and agent = :'sage'),
    'Can delete own agents'
);

delete from agents where uid = :'bob' and agent = :'phoenix';
select ok(
    exists(select 1 from agents where uid = :'bob' and agent = :'phoenix'),
    'Cannot delete agents on behalf of another user'
);
-- endregion:   delete
-- endregion:   authenticated

select * from finish();
rollback;