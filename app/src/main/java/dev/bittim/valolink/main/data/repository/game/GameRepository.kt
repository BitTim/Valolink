package dev.bittim.valolink.main.data.repository.game

import kotlinx.coroutines.flow.Flow

interface GameRepository<T> {
    suspend fun getByUuid(uuid: String): Flow<T?>
    suspend fun getAll(): Flow<List<T>>

    suspend fun fetch(uuid: String, version: String)
    suspend fun fetchAll(version: String)

    fun queueWorker(uuid: String? = null)
}