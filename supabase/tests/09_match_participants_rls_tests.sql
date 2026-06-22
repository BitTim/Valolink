-- Tests for RLS policies on the match_participants table using seed data

\set alice          00000000-0000-0000-0000-000000000001
\set bob            00000000-0000-0000-0000-000000000002
\set carol          00000000-0000-0000-0000-000000000003
\set dave           00000000-0000-0000-0000-000000000004
\set erin           00000000-0000-0000-0000-000000000005
\set fred           00000000-0000-0000-0000-000000000006
\set grace          00000000-0000-0000-0000-000000000007
\set hans           00000000-0000-0000-0000-000000000008
\set nonexisting    00000000-0000-0000-0000-000000000099

\set activity18     fff00000-0000-0000-0000-000000000012

\set match1       ccc00000-0000-0000-0000-000000000001
\set match2       ccc00000-0000-0000-0000-000000000002
\set match3       ccc00000-0000-0000-0000-000000000003
\set match4       ccc00000-0000-0000-0000-000000000004
\set match5       ccc00000-0000-0000-0000-000000000005
\set match6       ccc00000-0000-0000-0000-000000000006

begin;
select plan(15);

-- region:      anon
-- Impersonate anon role
set local role anon;

select is_empty(
    $$ select * from match_participants $$,
    'Anon cannot read any match_participants'
);

select throws_ok(
    $$ insert into match_participants (user_id, match) values ('$$ || :'alice' || $$', '$$ || :'match1' || $$') $$,
    42501, -- RLS violation
    null,
    'Anon cannot insert match_participants'
);

update match_participants set is_team_b = true where user_id = :'alice' and match = :'match1';
delete from match_participants where user_id = :'alice' and match = :'match2';
set local role postgres;

select is(
    (select is_team_b from match_participants where user_id = :'alice' and match = :'match1'),
    false,
    'Anon cannot update any match_participants'
);

select ok(
    exists(select 1 from match_participants where user_id = :'alice' and match = :'match2'),
    'Anon cannot delete any match_participants'
);
-- endregion:   anon

-- region:      authenticated
-- Impersonate Alice
set local role authenticated;
set local request.jwt.claims to '{"sub": "00000000-0000-0000-0000-000000000001"}';

-- region:      select
select ok(
    exists(select 1 from match_participants where user_id = :'alice'),
    'Can read own match_participants'
);

select ok(
    exists(select 1 from match_participants where user_id = :'bob'),
    'Can read public profiles match_participants'
);

select ok(
    exists(select 1 from match_participants where user_id = :'carol'),
    'Can read accepted private profiles match_participants'
);

select ok(
    not exists(select 1 from match_participants where user_id = :'dave'),
    'Cannot read pending private profiles match_participants'
);

select ok(
    not exists(select 1 from match_participants where user_id = :'fred'),
    'Cannot read private profiles match_participants'
);
-- endregion:   select

-- region:      insert
select lives_ok(
    $$ insert into activities (id, user_id, type) values ('$$ || :'activity18' || $$', '$$ || :'alice' || $$', 'MATCH');
    insert into match_participants (user_id, match, activity) values ('$$ || :'alice' || $$', '$$ || :'match4' || $$', '$$ || :'activity18' || $$') $$,
    'Can insert own match_participants'
);

select throws_ok(
    $$ insert into activities (id, user_id, type) values ('$$ || :'activity18' || $$', '$$ || :'bob' || $$', 'MATCH');
    insert into match_participants (user_id, match, activity) values ('$$ || :'bob' || $$', '$$ || :'match4' || $$', '$$ || :'activity18' || $$') $$,
    42501, -- RLS violation
    null,
    'Cannot insert match_participants on behalf of another user'
);
-- endregion:   insert

-- region:      update
update match_participants set is_team_b = true where user_id = :'alice' and match = :'match1';
select is(
    (select is_team_b from match_participants where user_id = :'alice' and match = :'match1'),
    true,
    'Can update own match_participants'
);

update match_participants set is_team_b = true where user_id = :'bob' and match = :'match3';
select is(
    (select is_team_b from match_participants where user_id = :'bob' and match = :'match3'),
    false,
    'Cannot update match_participants on behalf of another user'
);
-- endregion:   update

-- region:      delete
delete from match_participants where user_id = :'alice' and match = :'match2';
select ok(
    not exists(select 1 from match_participants where user_id = :'alice' and match = :'match2'),
    'Can delete own match_participants'
);

delete from match_participants where user_id = :'bob' and match = :'match3';
select ok(
    exists(select 1 from match_participants where user_id = :'bob' and match = :'match3'),
    'Cannot delete match_participants on behalf of another user'
);
-- endregion:   delete
-- endregion:   authenticated

select * from finish();
rollback;