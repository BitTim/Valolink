---
supersededBy: "[[(ADR-017) Split visible RR from internal RR and fold PLACEMENT activity into MATCH]]"
---
## Context
Matches, Placements, RR refunds and XP corrections were treated a separate concepts and would require separate tables.
## Decision
Unify Matches, Placements, RR refunds and XP corrections into a unified `activities` table with shared fields and a type discriminator.
## Consequences
- Calculation of XP and RR no longer requires join over 4 tables
- All concepts need to share a few fields, even if not relevant for the type (Like RR on XP refund)
