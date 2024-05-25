package dev.bittim.valolink.feature.content.data.repository.game

import dev.bittim.valolink.feature.content.domain.model.game.PlayerTitle
import kotlinx.coroutines.flow.Flow

interface PlayerTitleRepository {
    suspend fun getPlayerTitle(uuid: String, providedVersion: String? = null): Flow<PlayerTitle>

    suspend fun fetchPlayerTitle(uuid: String, version: String)
}