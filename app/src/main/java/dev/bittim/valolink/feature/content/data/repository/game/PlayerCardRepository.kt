package dev.bittim.valolink.feature.content.data.repository.game

import dev.bittim.valolink.feature.content.domain.model.PlayerCard
import kotlinx.coroutines.flow.Flow

interface PlayerCardRepository {
    suspend fun getPlayerCard(uuid: String, providedVersion: String? = null): Flow<PlayerCard>

    suspend fun fetchPlayerCard(uuid: String, version: String)
}