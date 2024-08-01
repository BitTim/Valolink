package dev.bittim.valolink.main.ui.screens.onboarding.getstarted

import dev.bittim.valolink.main.domain.model.user.UserAgent
import dev.bittim.valolink.main.domain.model.user.UserContract

data class GetStartedState(
    val loadingFinished: Int = 0,
    val userAgents: List<UserAgent> = emptyList(),
    val userContracts: List<UserContract> = emptyList(),
)
