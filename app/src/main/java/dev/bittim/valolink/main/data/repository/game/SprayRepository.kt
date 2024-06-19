package dev.bittim.valolink.main.data.repository.game

import dev.bittim.valolink.main.domain.model.game.Spray
import kotlinx.coroutines.flow.Flow

interface SprayRepository {
    suspend fun getByUuid(
        uuid: String,
        providedVersion: String? = null,
    ): Flow<Spray>

    suspend fun getAll(providedVersion: String?): Flow<List<Spray>>

    suspend fun fetch(
        uuid: String,
        version: String,
    )

    suspend fun fetchAll(version: String)

    fun queueWorker(uuid: String? = null)
}