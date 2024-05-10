package dev.bittim.valolink.feature.content.data.repository.game

import dev.bittim.valolink.feature.content.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun getEvent(uuid: String, providedVersion: String? = null): Flow<Event>
    suspend fun getAllEvents(providedVersion: String? = null): Flow<List<Event>>

    suspend fun fetchEvent(uuid: String, version: String)
    suspend fun fetchEvents(version: String)
}