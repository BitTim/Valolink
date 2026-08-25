---
supersededBy:
---
## Context
Storing unlocked state for levels risks drifting out of sync from the underlying XP data.
## Decision
Only store purchased levels and derive unlocked state from total XP from the activity
## Consequences
- Slightly more computation needed
- No more risk of data drift

