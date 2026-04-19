-- Tests for RLS policies on the matches table using seed data

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
\set match7       ccc00000-0000-0000-0000-000000000007

\set map1           ddd00000-0000-0000-0000-000000000001
\set map2           ddd00000-0000-0000-0000-000000000002
\set map3           ddd00000-0000-0000-0000-000000000003

\set mode1          eee00000-0000-0000-0000-000000000001
\set mode2          eee00000-0000-0000-0000-000000000002

begin;
select plan(10);

-- region:      anon
-- Impersonate anon role
set local role anon;

select is_empty(
    $$ select * from matches $$,
    'Anon cannot read any match'
);

select throws_ok(
    $$ insert into matches (id, map, mode) values ('$$ || :'match1' || $$', '$$ || :'map1' || $$', '$$ || :'mode1' || $$') $$,
    42501, -- RLS violation
    null,
    'Anon cannot insert match'
);

update matches set score_a = 1 where id = :'match1';
delete from matches where id = :'match2';
set local role postgres;

select is(
    (select score_a from matches where id = :'match1'),
    13,
    'Anon cannot update any match'
);

select ok(
    exists(select 1 from matches where id = :'match2'),
    'Anon cannot delete any match'
);

-- endregion:   anon

-- region:      authenticated
-- Impersonate Alice
set local role authenticated;
set local request.jwt.claims to '{"sub": "00000000-0000-0000-0000-000000000001"}';

-- region:      select
select ok(
    exists(select 1 from matches),
    'Can read all matches'
);
-- endregion:   select

-- region:      insert
select lives_ok(
    $$ set constraints match_participants_match_fkey deferred;
    insert into activities (id, user_id, type) values ('$$ || :'activity18' || $$', '$$ || :'alice' || $$', 'MATCH');
    insert into match_participants (user_id, match, activity, is_owner) values ('$$ || :'alice' || $$', '$$ || :'match7' || $$', '$$ || :'activity18' || $$', true);
    insert into matches (id, map, mode) values ('$$ || :'match7' || $$', '$$ || :'map2' || $$', '$$ || :'mode2' || $$'); $$,
    'Can insert match'
);
-- endregion:   insert

-- region:      update
update matches set score_a = 999 where id = :'match1';
select is(
    (select score_a from matches where id = :'match1'),
    999,
    'Can update match as owner'
);

update matches set score_a = 888 where id = :'match3';
select is(
    (select score_a from matches where id = :'match3'),
    13,
    'Cannot update match if not owner'
);
-- endregion:   update

-- region:      delete
delete from matches where id = :'match1';
select ok(
    exists(select 1 from matches where id = :'match1'),
    'Cannot delete match as owner'
);

delete from matches where id = :'match3';
select ok(
    exists(select 1 from matches where id = :'match3'),
    'Cannot delete match if not owner'
);
-- endregion:   delete
-- endregion:   authenticated

select * from finish();
rollback;