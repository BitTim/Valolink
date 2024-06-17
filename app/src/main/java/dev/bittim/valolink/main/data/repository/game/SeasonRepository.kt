package dev.bittim.valolink.main.data.repository.game

import dev.bittim.valolink.main.domain.model.game.Season
import kotlinx.coroutines.flow.Flow

interface SeasonRepository {
    suspend fun getByUuid(
        uuid: String,
        providedVersion: String? = null,
    ): Flow<Season>

    suspend fun getAll(providedVersion: String? = null): Flow<List<Season>>

    suspend fun fetch(
        uuid: String,
        version: String,
    )

    suspend fun fetchAll(version: String)
    fun queueWorker(version: String, uuid: String? = null)
}