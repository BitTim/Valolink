---
superseded-by:
---
## Context
Having a companion app on the PC that auto generates match data from in-game hooks needs a way to communicate with the app. Before [[(ADR-001) Local only]] it would have been via account syncing and cloud data storage.
## Decision
Use QR-Codes as the interface between companion and app and also make matches shareable via QR-Codes in the same way as well.
## Consequences
No need for complex P2P networking, Bluetooth connectivity or other wireless technologies and also providing a simple sharing experience as well.
