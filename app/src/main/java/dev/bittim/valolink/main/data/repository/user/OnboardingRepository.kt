package dev.bittim.valolink.main.data.repository.user

import dev.bittim.valolink.main.domain.model.user.Progression

interface OnboardingRepository {
    suspend fun setOnboardingComplete(
        ownedAgentUuids: List<String>,
        progressions: List<Progression>,
    ): Boolean
}