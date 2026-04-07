-- Tests for RLS policies on the matche_details table using seed data

\set alice          00000000-0000-0000-0000-000000000001
\set bob            00000000-0000-0000-0000-000000000002
\set carol          00000000-0000-0000-0000-000000000003
\set dave           00000000-0000-0000-0000-000000000004
\set erin           00000000-0000-0000-0000-000000000005
\set fred           00000000-0000-0000-0000-000000000006
\set grace          00000000-0000-0000-0000-000000000007
\set hans           00000000-0000-0000-0000-000000000008
\set nonexisting    00000000-0000-0000-0000-000000000099

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
select plan(10);

-- region:      anon
-- Impersonate anon role
set local role anon;

select is_empty(
    $$ select * from match_details $$,
    'Anon cannot read any match details'
);

select throws_ok(
    $$ insert into match_details (id, map, mode) values ('$$ || :'details1' || $$', '$$ || :'map1' || $$', '$$ || :'mode1' || $$') $$,
    42501, -- RLS violation
    null,
    'Anon cannot insert match details'
);

update match_details set score_a = 1 where id = :'details1';
delete from match_details where id = :'details2';
set local role postgres;

select is(
    (select score_a from match_details where id = :'details1'),
    13,
    'Anon cannot update any match details'
);

select ok(
    exists(select 1 from match_details where id = :'details2'),
    'Anon cannot delete any match details'
);

-- endregion:   anon

-- region:      authenticated
-- Impersonate Alice
set local role authenticated;
set local request.jwt.claims to '{"sub": "00000000-0000-0000-0000-000000000001"}';

-- region:      select
select ok(
    exists(select 1 from match_details),
    'Can read all match details'
);
-- endregion:   select

-- region:      insert
select lives_ok(
    $$ set constraints matches_details_fkey deferred;
    insert into matches (uid, details, is_owner) values ('$$ || :'alice' || $$', '$$ || :'details7' || $$', true);
    insert into match_details (id, map, mode) values ('$$ || :'details7' || $$', '$$ || :'map2' || $$', '$$ || :'mode2' || $$'); $$,
    'Can insert match details'
);
-- endregion:   insert

-- region:      update
update match_details set score_a = 999 where id = :'details1';
select is(
    (select score_a from match_details where id = :'details1'),
    999,
    'Can update match details as owner'
);

update match_details set score_a = 888 where id = :'details3';
select is(
    (select score_a from match_details where id = :'details3'),
    13,
    'Cannot update match details if not owner'
);
-- endregion:   update

-- region:      delete
delete from match_details where id = :'details1';
select ok(
    exists(select 1 from match_details where id = :'details1'),
    'Cannot delete match details as owner'
);

delete from match_details where id = :'details3';
select ok(
    exists(select 1 from match_details where id = :'details3'),
    'Cannot delete match details if not owner'
);
-- endregion:   delete
-- endregion:   authenticated

select * from finish();
rollback;