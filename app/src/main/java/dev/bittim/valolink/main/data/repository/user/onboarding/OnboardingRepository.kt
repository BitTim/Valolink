package dev.bittim.valolink.main.data.repository.user.onboarding

import dev.bittim.valolink.main.domain.model.user.UserAgent
import dev.bittim.valolink.main.domain.model.user.UserContract

interface OnboardingRepository {
    suspend fun setOnboardingComplete(
        agents: List<UserAgent>,
        userContracts: List<UserContract>,
    ): Boolean
}