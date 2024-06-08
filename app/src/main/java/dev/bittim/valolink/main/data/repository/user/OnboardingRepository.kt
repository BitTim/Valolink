package dev.bittim.valolink.main.data.repository.user

import dev.bittim.valolink.main.domain.model.Gear

interface OnboardingRepository {
    suspend fun setOnboardingComplete(
        ownedAgentUuids: List<String>,
        gears: List<Gear>,
    ): Boolean
}