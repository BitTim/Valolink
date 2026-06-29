---
supersededBy: ADR-020
---
## Context
Filtering activities by season via timestamp joins is expensive at query time.
## Decision
Add a `season` column on `activities` that gets updated by a trigger.
## Consequences
- Cheaper and simpler querying
- Added complexity with maintaining the trigger

