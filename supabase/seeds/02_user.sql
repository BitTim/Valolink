-- =====================
-- Users
-- =====================
insert into public.users (id, username, is_private)
values
    -- public, fully onboarded with rank
    ('00000000-0000-0000-0000-000000000001', 'Alice', false),
    -- public, fully onboarded no rank
    ('00000000-0000-0000-0000-000000000002', 'Bob', false),
    -- private, fully onboarded with rank
    ('00000000-0000-0000-0000-000000000003', 'Carol', true),
    -- private, not yet onboarded
    ('00000000-0000-0000-0000-000000000004', 'Dave', true),
    -- public, fully onboarded with rank
    ('00000000-0000-0000-0000-000000000005', 'Erin', false),
    -- private, fully onboarded without rank
    ('00000000-0000-0000-0000-000000000006', 'Fred', true),
    -- private, fully onboarded with rank
    ('00000000-0000-0000-0000-000000000007', 'Grace', true),
    -- private, fully onboarded with rank
    ('00000000-0000-0000-0000-000000000008', 'Hans', true)
on conflict (id) do update
    set username = excluded.username,
        is_private = excluded.is_private;
    
-- =====================
-- Flags
-- =====================
insert into public.flags (user_id, has_onboarded)
values
    -- public, fully onboarded with rank
    ('00000000-0000-0000-0000-000000000001', true),
    -- public, fully onboarded no rank
    ('00000000-0000-0000-0000-000000000002', true),
    -- private, fully onboarded with rank
    ('00000000-0000-0000-0000-000000000003', true),
    -- private, not yet onboarded
    ('00000000-0000-0000-0000-000000000004', false),
    -- public, fully onboarded with rank
    ('00000000-0000-0000-0000-000000000005', true),
    -- private, fully onboarded without rank
    ('00000000-0000-0000-0000-000000000006', true),
    -- private, fully onboarded with rank
    ('00000000-0000-0000-0000-000000000007', true),
    -- private, fully onboarded with rank
    ('00000000-0000-0000-0000-000000000008', true)
on conflict (user_id) do update
    set has_onboarded = excluded.has_onboarded;

-- =====================
-- Follows
-- =====================
insert into public.follows (follower, following, status)
values
    -- Alice follows Bob (public, auto-accepted)
    ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000002', 'ACCEPTED'),
    -- Alice follows Carol (private, accepted)
    ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000003', 'ACCEPTED'),
    -- Alice follows Dave (private, pending)
    ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000004', 'PENDING'),
    -- Bob follows Alice (public, auto-accepted) → Alice and Bob are mutuals
    ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000001', 'ACCEPTED'),
    -- Carol follows Alice (public, auto-accepted) → Alice and Carol are mutuals
    ('00000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000001', 'ACCEPTED'),
    -- Erin follows Carol (private, pending)
    ('00000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000003', 'PENDING'),
    -- Bob follows Dave (private, accepted)
    ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000004', 'ACCEPTED'),
    -- Bob follows Erin (public, auto-accepted)
    ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000005', 'ACCEPTED'),
    -- Bob follows Fred (private, pending)
    ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000006', 'PENDING'),
    -- Carol follows Bob (public, auto-accepted)
    ('00000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000002', 'ACCEPTED'),
    -- Alice follows Grace (private, accepted)
    ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000007', 'ACCEPTED'),
    -- Carol follows Grace (private, accepted)
    ('00000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000007', 'ACCEPTED'),
    -- Carol follows Dave (private, accepted)
    ('00000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000004', 'ACCEPTED'),
    -- Carol follows Fred (private, accepted)
    ('00000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000006', 'ACCEPTED'),
    -- Dave follows Bob (public, auto-accepted)
    ('00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000002', 'ACCEPTED'),
    -- Fred follows Alice (public, auto-accepted)
    ('00000000-0000-0000-0000-000000000006', '00000000-0000-0000-0000-000000000001', 'ACCEPTED'),
    -- Fred follows Bob (public, auto-accepted)
    ('00000000-0000-0000-0000-000000000006', '00000000-0000-0000-0000-000000000002', 'ACCEPTED'),
    -- Hans follows Grace (private, pending)
    ('00000000-0000-0000-0000-000000000008', '00000000-0000-0000-0000-000000000007', 'PENDING');

-- =====================
-- Agents
-- Fake agent UUIDs simulating valorant-api.com UUIDs
-- =====================
insert into public.agents (user_id, agent)
values
    -- Alice owns jett and sage
    ('00000000-0000-0000-0000-000000000001', 'a3bfb853-43b2-7238-a4f1-ad90e9e46bcc'), -- jett
    ('00000000-0000-0000-0000-000000000001', '569fdd95-4d10-43ab-ca70-79becc718b46'), -- sage
    -- Bob owns phoenix
    ('00000000-0000-0000-0000-000000000002', 'eb93336a-449b-9c1e-0a7d-5bae39493506'), -- phoenix
    -- Carol owns jett and reyna
    ('00000000-0000-0000-0000-000000000003', 'a3bfb853-43b2-7238-a4f1-ad90e9e46bcc'), -- jett
    ('00000000-0000-0000-0000-000000000003', 'f94c3b30-42be-e959-889c-5aa313dba261'), -- reyna
    -- Dave owns sage
    ('00000000-0000-0000-0000-000000000004', '569fdd95-4d10-43ab-ca70-79becc718b46'), -- sage
    -- Erin owns sage and reyna
    ('00000000-0000-0000-0000-000000000005', '569fdd95-4d10-43ab-ca70-79becc718b46'), -- sage
    ('00000000-0000-0000-0000-000000000005', 'f94c3b30-42be-e959-889c-5aa313dba261'), -- reyna
    -- Fred owns reyna
    ('00000000-0000-0000-0000-000000000006', 'f94c3b30-42be-e959-889c-5aa313dba261'); -- reyna

-- =====================
-- Progressions
-- Fake progression UUIDs simulating valorant-api.com UUIDs
-- =====================
insert into public.progressions (user_id, progression, free_only, xp_offset)
values
    -- Alice has two progressions, one free only
    ('00000000-0000-0000-0000-000000000001', 'aaa00000-0000-0000-0000-000000000001', false, 0),
    ('00000000-0000-0000-0000-000000000001', 'aaa00000-0000-0000-0000-000000000002', true, 5000),
    -- Bob has one progression with xp offset (had progress before tracking)
    ('00000000-0000-0000-0000-000000000002', 'aaa00000-0000-0000-0000-000000000001', false, 12000),
    -- Carol has one progression
    ('00000000-0000-0000-0000-000000000003', 'aaa00000-0000-0000-0000-000000000003', false, 0),
    -- Dave has one progression
    ('00000000-0000-0000-0000-000000000004', 'aaa00000-0000-0000-0000-000000000002', false, 0),
    -- Erin has one progression
    ('00000000-0000-0000-0000-000000000005', 'aaa00000-0000-0000-0000-000000000001', false, 0),
    -- Fred has one progression
    ('00000000-0000-0000-0000-000000000006', 'aaa00000-0000-0000-0000-000000000003', false, 0);

-- =====================
-- Purchased Levels
-- Fake level UUIDs simulating valorant-api.com UUIDs
-- =====================
insert into public.purchased_levels (user_id, progression, level)
values
    -- Alice progression 1: levels 1, 2 and 3 purchased
    ('00000000-0000-0000-0000-000000000001', 'aaa00000-0000-0000-0000-000000000001', 'bbb00000-0000-0000-0000-000000000001'),
    ('00000000-0000-0000-0000-000000000001', 'aaa00000-0000-0000-0000-000000000001', 'bbb00000-0000-0000-0000-000000000002'),
    ('00000000-0000-0000-0000-000000000001', 'aaa00000-0000-0000-0000-000000000001', 'bbb00000-0000-0000-0000-000000000003'),
    -- Alice progression 2: level 1 purchased
    ('00000000-0000-0000-0000-000000000001', 'aaa00000-0000-0000-0000-000000000002', 'bbb00000-0000-0000-0000-000000000001'),
    -- Bob progression 1: levels 1 and 2 purchased
    ('00000000-0000-0000-0000-000000000002', 'aaa00000-0000-0000-0000-000000000001', 'bbb00000-0000-0000-0000-000000000001'),
    ('00000000-0000-0000-0000-000000000002', 'aaa00000-0000-0000-0000-000000000001', 'bbb00000-0000-0000-0000-000000000002'),
    -- Carol progression 3: level 1 purchased
    ('00000000-0000-0000-0000-000000000003', 'aaa00000-0000-0000-0000-000000000003', 'bbb00000-0000-0000-0000-000000000001'),
    -- Dave progression 2: level 1 purchased
    ('00000000-0000-0000-0000-000000000004', 'aaa00000-0000-0000-0000-000000000002', 'bbb00000-0000-0000-0000-000000000001'),
    -- Fred progression 3: level 1 purchased
    ('00000000-0000-0000-0000-000000000006', 'aaa00000-0000-0000-0000-000000000003', 'bbb00000-0000-0000-0000-000000000001');

-- =====================
-- Activities
-- =====================

insert into public.activities (id, user_id, type, xp, rr)
values
    -- Alice has a placement, 3 matches and an XP correction
    ('fff00000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'PLACEMENT', 0, 350),
    ('fff00000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000001', 'MATCH', 4200, 22),
    ('fff00000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000001', 'MATCH', 3100, 18),
    ('fff00000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000001', 'XP_CORRECTION', 100, null),
    ('fff00000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000001', 'MATCH', 2000, 20),
    -- Bob has a placement adn 3 matches
    ('fff00000-0000-0000-0000-000000000006', '00000000-0000-0000-0000-000000000002', 'PLACEMENT', 0, 350),
    ('fff00000-0000-0000-0000-000000000007', '00000000-0000-0000-0000-000000000002', 'MATCH', 3800, 15),
    ('fff00000-0000-0000-0000-000000000008', '00000000-0000-0000-0000-000000000002', 'MATCH', 2100, -8),
    ('fff00000-0000-0000-0000-000000000009', '00000000-0000-0000-0000-000000000002', 'MATCH', 2100, 17),
    -- Carol has a placement, 2 matches and an RR refund
    ('fff00000-0000-0000-0000-00000000000a', '00000000-0000-0000-0000-000000000003', 'PLACEMENT', 0, 350),
    ('fff00000-0000-0000-0000-00000000000b', '00000000-0000-0000-0000-000000000003', 'MATCH', 2900, -12),
    ('fff00000-0000-0000-0000-00000000000c', '00000000-0000-0000-0000-000000000003', 'MATCH', 1500, -18),
    ('fff00000-0000-0000-0000-00000000000d', '00000000-0000-0000-0000-000000000002', 'RR_REFUND', 0, 12),
    -- Dave has a placement and a match
    ('fff00000-0000-0000-0000-00000000000e', '00000000-0000-0000-0000-000000000004', 'PLACEMENT', 0, 350),
    ('fff00000-0000-0000-0000-00000000000f', '00000000-0000-0000-0000-000000000004', 'MATCH', 2800, -18),
    -- Fred has a placement and a match
    ('fff00000-0000-0000-0000-000000000010', '00000000-0000-0000-0000-000000000006', 'PLACEMENT', 0, 350),
    ('fff00000-0000-0000-0000-000000000011', '00000000-0000-0000-0000-000000000006', 'MATCH', 3200, 12);


-- =====================
-- Matches
-- Fake map and mode UUIDs simulating valorant-api.com UUIDs
-- =====================
insert into public.matches (id, score_a, score_b, end_reason, time, map, mode)
values
    -- match 1: Alice owns, completed, team a wins 13-7
    ('ccc00000-0000-0000-0000-000000000001', 13, 7,  'COMPLETED',   now() - interval '2 days', 'ddd00000-0000-0000-0000-000000000001', 'eee00000-0000-0000-0000-000000000001'),
    -- match 2: Alice owns, team b surrenders
    ('ccc00000-0000-0000-0000-000000000002', 6, 3,  'SURRENDER_B', now() - interval '1 day',  'ddd00000-0000-0000-0000-000000000002', 'eee00000-0000-0000-0000-000000000001'),
    -- match 3: Bob owns, completed, close game
    ('ccc00000-0000-0000-0000-000000000003', 13, 11, 'COMPLETED',   now() - interval '3 days', 'ddd00000-0000-0000-0000-000000000001', 'eee00000-0000-0000-0000-000000000002'),
    -- match 4: Carol owns (private user)
    ('ccc00000-0000-0000-0000-000000000004', 8,  13, 'COMPLETED',   now() - interval '4 days', 'ddd00000-0000-0000-0000-000000000003', 'eee00000-0000-0000-0000-000000000001'),
    -- match 5: Alice owns, Bob tags on
    ('ccc00000-0000-0000-0000-000000000005', 13, 9,  'COMPLETED',   now() - interval '5 days', 'ddd00000-0000-0000-0000-000000000002', 'eee00000-0000-0000-0000-000000000002'),
    -- match 6: Carol owns (private user), Bob tags on
    ('ccc00000-0000-0000-0000-000000000006', 2, 7,  'SURRENDER_A',   now() - interval '6 days', 'ddd00000-0000-0000-0000-000000000002', 'eee00000-0000-0000-0000-000000000002');

-- =====================
-- Match Participants
-- =====================
insert into public.match_participants (user_id, activity, match, is_owner, is_team_b)
values
    -- Alice in match 1 (team a, won) — Alice owns the match
    ('00000000-0000-0000-0000-000000000001', 'fff00000-0000-0000-0000-000000000002', 'ccc00000-0000-0000-0000-000000000001', true, false),
    -- Alice in match 2 (team a, won via surrender) — Alice owns the match
    ('00000000-0000-0000-0000-000000000001', 'fff00000-0000-0000-0000-000000000003', 'ccc00000-0000-0000-0000-000000000002', true, false),
    -- Bob in match 3 (team a, won) — Bob owns the match
    ('00000000-0000-0000-0000-000000000002', 'fff00000-0000-0000-0000-000000000007', 'ccc00000-0000-0000-0000-000000000003', true, false),
    -- Carol in match 4 (team b, lost) — Carol owns the match
    ('00000000-0000-0000-0000-000000000003', 'fff00000-0000-0000-0000-00000000000b', 'ccc00000-0000-0000-0000-000000000004', true, true),
    -- Alice in match 5 (team a, won) — Alice owns the match
    ('00000000-0000-0000-0000-000000000001', 'fff00000-0000-0000-0000-000000000005', 'ccc00000-0000-0000-0000-000000000005', true, false),
    -- Bob tags onto match 5 (team b, lost) — Alice owns the match
    ('00000000-0000-0000-0000-000000000002', 'fff00000-0000-0000-0000-000000000008', 'ccc00000-0000-0000-0000-000000000005', false, true),
    -- Carol in match 6 (team a, lost via surrender) — Carol owns the match
    ('00000000-0000-0000-0000-000000000003', 'fff00000-0000-0000-0000-00000000000c', 'ccc00000-0000-0000-0000-000000000006', true, false),
    -- Bob tags onto match 6 (team b, won via surrender) — Carol owns the match
    ('00000000-0000-0000-0000-000000000002', 'fff00000-0000-0000-0000-000000000009', 'ccc00000-0000-0000-0000-000000000006', false, true),
    -- Dave in match 2 (team b, lost via surrender) — Alice owns the match
    ('00000000-0000-0000-0000-000000000004', 'fff00000-0000-0000-0000-00000000000f', 'ccc00000-0000-0000-0000-000000000002', false, true),
    -- Fred in match 4 (team a, won) — Carol owns the match
    ('00000000-0000-0000-0000-000000000006', 'fff00000-0000-0000-0000-000000000011', 'ccc00000-0000-0000-0000-000000000004', false, false);