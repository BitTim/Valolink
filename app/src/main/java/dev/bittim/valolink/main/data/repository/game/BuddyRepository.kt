package dev.bittim.valolink.main.data.repository.game

import dev.bittim.valolink.main.domain.model.game.buddy.Buddy
import kotlinx.coroutines.flow.Flow

interface BuddyRepository {
    suspend fun getByUuid(
        uuid: String,
        providedVersion: String? = null,
    ): Flow<Buddy>

    suspend fun getByLevelUuid(
        levelUuid: String,
        providedVersion: String? = null,
    ): Flow<Buddy>

    suspend fun getAll(
        providedVersion: String? = null,
    ): Flow<List<Buddy>>

    suspend fun fetch(
        uuid: String,
        version: String,
    )

    suspend fun fetchAll(
        version: String,
    )

    fun queueWorker(uuid: String? = null)
}