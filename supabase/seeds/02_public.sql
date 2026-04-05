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
    ('00000000-0000-0000-0000-000000000005', 'Erin', false)
on conflict (id) do update
    set username = excluded.username,
        is_private = excluded.is_private;
    
-- =====================
-- Flags
-- =====================
insert into public.flags (uid, has_onboarded, has_rank)
values
    -- public, fully onboarded with rank
    ('00000000-0000-0000-0000-000000000001', true, true),
    -- public, fully onboarded no rank
    ('00000000-0000-0000-0000-000000000002', true, false),
    -- private, fully onboarded with rank
    ('00000000-0000-0000-0000-000000000003', true, true),
    -- private, not yet onboarded
    ('00000000-0000-0000-0000-000000000004', false, false),
    -- public, fully onboarded with rank
    ('00000000-0000-0000-0000-000000000005', true, true)
on conflict (uid) do update
    set has_onboarded = excluded.has_onboarded,
        has_rank = excluded.has_rank;

-- =====================
-- Follows
-- =====================
insert into public.follows (follower, following, accepted)
values
    -- Alice follows Bob (public, auto-accepted)
    ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000002', true),
    -- Alice follows Carol (private, accepted)
    ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000003', true),
    -- Alice follows Dave (private, pending)
    ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000004', false),
    -- Bob follows Alice (public, auto-accepted) → Alice and Bob are mutuals
    ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000001', true),
    -- Carol follows Alice (public, auto-accepted) → Alice and Carol are mutuals
    ('00000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000001', true),
    -- Erin follows Carol (private, pending)
    ('00000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000003', false);

-- =====================
-- Agents
-- Fake agent UUIDs simulating valorant-api.com UUIDs
-- =====================
insert into public.agents (uid, agent)
values
    -- Alice owns jett and sage
    ('00000000-0000-0000-0000-000000000001', 'a3bfb853-43b2-7238-a4f1-ad90e9e46bcc'), -- jett
    ('00000000-0000-0000-0000-000000000001', '569fdd95-4d10-43ab-ca70-79becc718b46'), -- sage
    -- Bob owns phoenix
    ('00000000-0000-0000-0000-000000000002', 'eb93336a-449b-9c1e-0a7d-5bae39493506'), -- phoenix
    -- Carol owns jett and reyna
    ('00000000-0000-0000-0000-000000000003', 'a3bfb853-43b2-7238-a4f1-ad90e9e46bcc'), -- jett
    ('00000000-0000-0000-0000-000000000003', 'f94c3b30-42be-e959-889c-5aa313dba261'), -- reyna
    -- Erin owns sage and reyna
    ('00000000-0000-0000-0000-000000000005', '569fdd95-4d10-43ab-ca70-79becc718b46'), -- sage
    ('00000000-0000-0000-0000-000000000005', 'f94c3b30-42be-e959-889c-5aa313dba261'); -- reyna

-- =====================
-- Contracts
-- Fake contract UUIDs simulating valorant-api.com UUIDs
-- =====================
insert into public.contracts (uid, contract, free_only, xp_offset)
values
    -- Alice has two contracts, one free only
    ('00000000-0000-0000-0000-000000000001', 'aaa00000-0000-0000-0000-000000000001', false, 0),
    ('00000000-0000-0000-0000-000000000001', 'aaa00000-0000-0000-0000-000000000002', true, 5000),
    -- Bob has one contract with xp offset (had progress before tracking)
    ('00000000-0000-0000-0000-000000000002', 'aaa00000-0000-0000-0000-000000000001', false, 12000),
    -- Carol has one contract
    ('00000000-0000-0000-0000-000000000003', 'aaa00000-0000-0000-0000-000000000003', false, 0),
    -- Erin has one contract
    ('00000000-0000-0000-0000-000000000005', 'aaa00000-0000-0000-0000-000000000001', false, 0);

-- =====================
-- Levels
-- Fake level UUIDs simulating valorant-api.com UUIDs
-- =====================
insert into public.levels (uid, contract, level, is_purchased)
values
    -- Alice contract 1: levels 1 and 2 unlocked, level 3 purchased
    ('00000000-0000-0000-0000-000000000001', 'aaa00000-0000-0000-0000-000000000001', 'bbb00000-0000-0000-0000-000000000001', false),
    ('00000000-0000-0000-0000-000000000001', 'aaa00000-0000-0000-0000-000000000001', 'bbb00000-0000-0000-0000-000000000002', false),
    ('00000000-0000-0000-0000-000000000001', 'aaa00000-0000-0000-0000-000000000001', 'bbb00000-0000-0000-0000-000000000003', true),
    -- Alice contract 2: level 1 unlocked (free only)
    ('00000000-0000-0000-0000-000000000001', 'aaa00000-0000-0000-0000-000000000002', 'bbb00000-0000-0000-0000-000000000001', false),
    -- Bob contract 1: levels 1 and 2 unlocked
    ('00000000-0000-0000-0000-000000000002', 'aaa00000-0000-0000-0000-000000000001', 'bbb00000-0000-0000-0000-000000000001', false),
    ('00000000-0000-0000-0000-000000000002', 'aaa00000-0000-0000-0000-000000000001', 'bbb00000-0000-0000-0000-000000000002', false),
    -- Carol contract 3: level 1 unlocked
    ('00000000-0000-0000-0000-000000000003', 'aaa00000-0000-0000-0000-000000000003', 'bbb00000-0000-0000-0000-000000000001', false);

-- =====================
-- Match Details
-- Fake map and mode UUIDs simulating valorant-api.com UUIDs
-- =====================
insert into public.match_details (id, score_a, score_b, end_reason, time, map, mode)
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
-- Matches
-- =====================
insert into public.matches (uid, details, xp, rr, rr_offset, is_owner, is_team_b)
values
    -- Alice in match 1 (team a, won) — Alice owns the match_details
    ('00000000-0000-0000-0000-000000000001', 'ccc00000-0000-0000-0000-000000000001', 4200, 22,  0,  true, false),
    -- Alice in match 2 (team a, won via surrender) — Alice owns the match_details
    ('00000000-0000-0000-0000-000000000001', 'ccc00000-0000-0000-0000-000000000002', 3100, 18,  0,  true, false),
    -- Bob in match 3 (team a, won) — Bob owns the match_details
    ('00000000-0000-0000-0000-000000000002', 'ccc00000-0000-0000-0000-000000000003', 3800, 15,  0,  true, false),
    -- Carol in match 4 (team b, lost) — Carol owns the match_details
    ('00000000-0000-0000-0000-000000000003', 'ccc00000-0000-0000-0000-000000000004', 2900, -12, 0,  true, true),
    -- Alice in match 5 (team a, won) — Alice owns the match_details
    ('00000000-0000-0000-0000-000000000001', 'ccc00000-0000-0000-0000-000000000005', 3500, 20,  0,  true, false),
    -- Bob tags onto match 5 (team b, lost) — Alice owns the match_details
    ('00000000-0000-0000-0000-000000000002', 'ccc00000-0000-0000-0000-000000000005', 2100, -8,  0,  false, true),
    -- Carol in match 6 (team a, lost via surrender) — Carol owns the match_details
    ('00000000-0000-0000-0000-000000000003', 'ccc00000-0000-0000-0000-000000000006', 1500, -18,  0,  true, false),
    -- Bob tags onto match 6 (team b, won via surrender) — Carol owns the match_details
    ('00000000-0000-0000-0000-000000000002', 'ccc00000-0000-0000-0000-000000000006', 2100, 17,  0,  false, true);

-- =====================
-- Rel Match Contract
-- =====================
insert into public.rel_match_contract (uid, contract, details)
values
    -- Alice match 1 contributed to contract 1
    ('00000000-0000-0000-0000-000000000001', 'aaa00000-0000-0000-0000-000000000001', 'ccc00000-0000-0000-0000-000000000001'),
    -- Alice match 2 contributed to contract 1
    ('00000000-0000-0000-0000-000000000001', 'aaa00000-0000-0000-0000-000000000001', 'ccc00000-0000-0000-0000-000000000002'),
    -- Alice match 5 contributed to contract 2
    ('00000000-0000-0000-0000-000000000001', 'aaa00000-0000-0000-0000-000000000002', 'ccc00000-0000-0000-0000-000000000005'),
    -- Bob match 3 contributed to contract 1
    ('00000000-0000-0000-0000-000000000002', 'aaa00000-0000-0000-0000-000000000001', 'ccc00000-0000-0000-0000-000000000003'),
    -- Bob match 5 tag contributed to contract 1
    ('00000000-0000-0000-0000-000000000002', 'aaa00000-0000-0000-0000-000000000001', 'ccc00000-0000-0000-0000-000000000005'),
    -- Bob match 6 tag contributed to contract 1
    ('00000000-0000-0000-0000-000000000002', 'aaa00000-0000-0000-0000-000000000001', 'ccc00000-0000-0000-0000-000000000006'),
    -- Carol match 4 contributed to contract 3
    ('00000000-0000-0000-0000-000000000003', 'aaa00000-0000-0000-0000-000000000003', 'ccc00000-0000-0000-0000-000000000004'),
    -- Carol match 6 contributed to contract 3
    ('00000000-0000-0000-0000-000000000003', 'aaa00000-0000-0000-0000-000000000003', 'ccc00000-0000-0000-0000-000000000006');