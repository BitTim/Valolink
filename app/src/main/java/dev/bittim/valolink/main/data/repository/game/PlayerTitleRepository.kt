package dev.bittim.valolink.main.data.repository.game

import dev.bittim.valolink.main.domain.model.game.PlayerTitle
import kotlinx.coroutines.flow.Flow

interface PlayerTitleRepository {
    suspend fun getByUuid(
        uuid: String,
        providedVersion: String? = null,
    ): Flow<PlayerTitle>

    suspend fun getAll(providedVersion: String?): Flow<List<PlayerTitle>>

    suspend fun fetch(
        uuid: String,
        version: String,
    )

    suspend fun fetchAll(version: String)

    fun queueWorker(uuid: String? = null)
}