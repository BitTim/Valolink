package dev.bittim.valolink.main.ui.screens.onboarding.getstarted

import dev.bittim.valolink.user.domain.model.UserAgent
import dev.bittim.valolink.user.domain.model.UserContract

data class GetStartedState(
    val loadingFinished: Int = 0,
    val userAgents: List<UserAgent> = emptyList(),
    val userContracts: List<UserContract> = emptyList(),
)
