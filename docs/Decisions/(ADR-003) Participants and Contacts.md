---
superseded-by:
---
## Context
Keeping track of who you play with can also be beneficial and provide some useful insights, even if it is just out of interest or curiosity.
## Decision
Add a simple Contacts system that maps usernames and tags to local UUIDs, which can be appended to matches. The companion app and sharing via QR-Code like intended with [[(ADR-002) QR-Codes as interface]] would just share the usernames and tags of the match participants as part of the payload and will be matched against the recipients contact list.
## Consequences
A new module to architect and UI/UX to accommodate while being able to handle more dense information for processing
