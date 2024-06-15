package dev.bittim.valolink.main.ui.screens.onboarding.getstarted

import dev.bittim.valolink.main.domain.model.user.Gear

data class GetStartedState(
    val loadingFinished: Int = 0,
    val ownedAgentUuids: List<String> = listOf(),
    val gears: List<Gear> = listOf(),
)
