---
supersededBy:
---
## Context
Putting the match owners UUID into the `matches` table directly would expose private users UUIDs and also their matches, which defeats the purpose of having private users.
## Decision
Track ownership via `match_participants` instead, since these entries are scoped to the user and already apply the users visibility rules.
## Consequences
- Added complexity for keeping track of who the owner is and what happens when the ownership must change (User entry deletion)
- No data from private users is leaked
