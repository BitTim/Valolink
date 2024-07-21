package dev.bittim.valolink.main.ui.screens.onboarding.getstarted

import dev.bittim.valolink.main.domain.model.user.Progression

data class GetStartedState(
    val loadingFinished: Int = 0,
    val ownedAgentUuids: List<String> = emptyList(),
    val progressions: List<Progression> = emptyList(),
)
