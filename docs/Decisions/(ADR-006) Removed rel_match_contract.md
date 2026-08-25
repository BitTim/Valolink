---
supersededBy:
---
## Context
The `rel_match_contract` table was supposed to track what contracts a match contributed XP towards. This added complexity and risk for stale data.
## Decision
The XP contribution will be derived via timestamp on the match and the start and end time of the contract.
## Consequences
- Less complexity and less risk for stale data
- Computationally slightly more expensive
- No explicit relations between matches and contracts anymore

