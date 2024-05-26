package dev.bittim.valolink.feature.main.data.repository.game

import dev.bittim.valolink.feature.main.domain.model.game.buddy.BuddyLevel
import kotlinx.coroutines.flow.Flow

interface BuddyLevelRepository {
    suspend fun getBuddyLevel(uuid: String, providedVersion: String? = null): Flow<BuddyLevel>

    suspend fun fetchBuddyLevel(uuid: String, version: String)
}