---
supersededBy: ADR-021 (Partial)
---
## Context
Caching every seasons full activity locally does not scale and is not needed for the most part.
## Decision
Paginate activity fetching by season and only keep the current seasons activity in Room while fetching other seasons activities from remote on demand.
## Consequences
- Less time spent on caching

