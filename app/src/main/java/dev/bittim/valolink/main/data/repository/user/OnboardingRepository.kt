package dev.bittim.valolink.main.data.repository.user

import dev.bittim.valolink.main.domain.model.user.Gear

interface OnboardingRepository {
    suspend fun setOnboardingComplete(
        ownedAgentUuids: List<String>,
        gears: List<Gear>,
    ): Boolean
}