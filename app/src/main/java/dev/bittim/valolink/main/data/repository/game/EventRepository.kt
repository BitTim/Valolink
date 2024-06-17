package dev.bittim.valolink.main.data.repository.game

import dev.bittim.valolink.main.domain.model.game.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun getByUuid(
        uuid: String,
        providedVersion: String? = null,
    ): Flow<Event>

    suspend fun getAll(providedVersion: String? = null): Flow<List<Event>>

    suspend fun fetch(
        uuid: String,
        version: String,
    )

    suspend fun fetchAll(version: String)
    fun queueWorker(version: String, uuid: String? = null)
}