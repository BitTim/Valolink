---
supersededBy:
---
## Context
Some user data needs to apply the users visibility rules while some data needs to stay public for discoverability.
## Decision
Split original `users` table into `users`, which holds discoverable data like the `username`, and `flags` which holds some flags that need to respect the users visibility rules, like `has_onboarded`.
## Consequences
- Adds another table that needs to be kept in sync
- Further hardens the schema for private users