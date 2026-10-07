---
supersededBy: ADR-019
---
## Context
Computing season stats on read would require fetching a users entire activity for the season every time.
## Decision
Maintain a `user_season_stats` table that updates via trigger to hold stats instead of recomputing on every read.
## Consequences
- Added complexity with maintaining the trigger
- Risk of data getting stale
- Cheaper and faster querying

