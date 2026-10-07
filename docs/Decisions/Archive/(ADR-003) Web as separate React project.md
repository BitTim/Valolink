---
superseded-by:
---
## Context
A web version is also planned, but the KMP Web target is not yet production ready and the Web UX differs significantly from Mobile UX so there is no benefit to sharing UI anyway.
## Decision
Build Web version as a standalone React project against the same Supabase backend instead of using the KMP Web target.
## Consequences
- More work to create and maintain a separate Web version
- Much improved UX for Web by not forcing mobile UX on Web

