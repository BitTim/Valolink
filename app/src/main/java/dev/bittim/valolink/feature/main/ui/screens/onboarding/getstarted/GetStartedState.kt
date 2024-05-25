package dev.bittim.valolink.feature.main.ui.screens.onboarding.getstarted

data class GetStartedState(
    val isLoading: Boolean = true,
    val ownedAgentUuids: List<String> = listOf(),
)
