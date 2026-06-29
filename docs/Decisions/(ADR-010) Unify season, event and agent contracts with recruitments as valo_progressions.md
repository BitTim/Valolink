---
supersededBy:
---
## Context
The API ([[valorant-api.com]]) provides the data as separate entities even though they will be treated the same in Valolink.
## Decision
In the fetching stage, unify season, event and agent contracts as well as recruitment data into the `valo_progressions` table.
## Consequences
- More computation during fetching
- Simpler to retrieve progression data
