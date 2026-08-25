---
supersededBy:
---
## Context
Considered putting a nullable participant column into `activities` to link actual match data to an activity. `match_participants` holds the per user match object.
## Decision
Have a Foreign Key to `activities` on the `match_participants` table so rich match data can be associated with an activity.
## Consequences
- `activities` does not get polluted with a field only relevant to one type of activity
