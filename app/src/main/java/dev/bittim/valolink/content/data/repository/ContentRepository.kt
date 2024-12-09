package dev.bittim.valolink.content.data.repository

import kotlinx.coroutines.flow.Flow

interface ContentRepository<T> {
    suspend fun getByUuid(uuid: String): Flow<T?>
    suspend fun getAll(): Flow<List<T>>

    suspend fun fetch(uuid: String, version: String)
    suspend fun fetchAll(version: String)

    fun queueWorker(uuid: String? = null)
}