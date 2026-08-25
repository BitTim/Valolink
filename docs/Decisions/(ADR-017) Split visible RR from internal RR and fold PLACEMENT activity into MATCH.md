---
supersededBy:
---
## Context
RR handling needs to distinguish between what Valorant shows the user and what is used internally for calculations. The PLACEMENT activity also added unnecessary complexity and risk for data drift.
## Decision
- `activities.rr` stays as the source of the internal RR
- `match_participants.visible_rr` is the raw RR value Valorant shows the user and will be displayed in the activity view as well.
- `matches.is_ranked` disambiguates `null` RR between being an unranked match or not having a rank yet
- PLACEMENT is no longer an activity type. A placement match will be the first MATCH with non-null RR. A placement can be reverted by setting RR to null again as long as no other match with non-null RR are present after the placement match.
- `matches.time` is the timestamp at what time the match was played. This is now also part of the shared data. `activities.time` duplicates the value.
## Consequences
- Solid base for proper RR calculation

