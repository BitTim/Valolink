package dev.bittim.valolink.main.data.repository.game

import dev.bittim.valolink.main.domain.model.game.PlayerCard
import kotlinx.coroutines.flow.Flow

interface PlayerCardRepository {
    suspend fun getByUuid(
        uuid: String,
        providedVersion: String? = null,
    ): Flow<PlayerCard>

    suspend fun getAll(providedVersion: String?): Flow<List<PlayerCard>>

    suspend fun fetch(
        uuid: String,
        version: String,
    )

    suspend fun fetchAll(version: String)

    fun queueWorker(version: String, uuid: String? = null)
}