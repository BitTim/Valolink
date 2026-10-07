---
supersededBy:
---
## Context
Server side triggers need access to content data without relying on clients to supply it.
## Decision
Cache all Valorant content data in Supabase with an edge function that runs all 6 hours and checks for new data.
## Consequences
- Server side triggers have access to content data
- Going easier on the external API since all clients hit my cache instead of the API directly

