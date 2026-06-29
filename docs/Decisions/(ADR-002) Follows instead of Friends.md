---
supersededBy:
---
## Context
Initial design used a `friends` table with a `friendship_status` enum. App features are asymmetric, like viewing match history does not require a mutual follow.
## Decision
Use a `follows` table for unidirectional, Instagram style follow relations. Mutual follows inferred from existing accepted rows in both directions.
## Consequences
- Simpler schema with no state enum needed
- Pending state with row existing but accepted being `false`
- Triggers for easier state management
- Mutual only features may be added later without schema change

