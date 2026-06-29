---
supersededBy:
---
## Context
Valolink was Android-only. Needed cross-platform from here on, now with the ability and need to ship iOS from the start. Wanted UI that feels native to each platform, which ruled out Flutter and React Native.
Already familiar with Kotlin/Jetpack Compose from the existing Android codebase.
## Decision
Move forward with Kotlin Multiplatform for shared business logic, and Compose Multiplatform for shared UI in the near term, to get both platforms shipped simultaneously.
## Consequences
- KMP/CMP ecosystem maturity risk
- Single shared codebase for business logic, reducing duplication
- CMP UI is native to Android but not truly native to iOS — treated as an interim step; revisiting Compose (Android) + SwiftUI (iOS) per platform is an open question for later, not decided here
