package dev.bittim.valolink.main.data.repository.game

import androidx.room.withTransaction
import dev.bittim.valolink.main.data.local.game.GameDatabase
import dev.bittim.valolink.main.data.remote.game.GameApi
import dev.bittim.valolink.main.domain.model.game.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class EventApiRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi,
    private val versionRepository: VersionRepository,
) : EventRepository {
    // --------------------------------
    //  Query from Database
    // --------------------------------

    // -------- [ Single queries ] --------

    override suspend fun getEvent(
        uuid: String,
        providedVersion: String?,
    ): Flow<Event> {
        val version = providedVersion ?: versionRepository.getApiVersion()?.version ?: ""

        return gameDatabase.eventDao.getEvent(uuid).distinctUntilChanged().transform { event ->
            if (event == null || event.version != version) {
                fetchEvent(
                    uuid,
                    version
                )
            } else {
                emit(event)
            }
        }.map { it.toType() }
    }

    // -------- [ Bulk queries ] --------

    override suspend fun getAllEvents(providedVersion: String?): Flow<List<Event>> {
        val version = providedVersion ?: versionRepository.getApiVersion()?.version ?: ""

        return gameDatabase.eventDao.getAllEvents().distinctUntilChanged().transform { events ->
            if (events.isEmpty() || events.any { it.version != version }) {
                fetchEvents(version)
            } else {
                emit(events)
            }
        }.map { seasons ->
            seasons.map { it.toType() }
        }
    }

    // --------------------------------
    //  Fetching from API
    // --------------------------------

    // -------- [ Single fetching ] --------

    override suspend fun fetchEvent(
        uuid: String,
        version: String,
    ) {
        val response = gameApi.getEvent(uuid)
        if (response.isSuccessful) {
            gameDatabase.withTransaction {
                gameDatabase.eventDao.upsertEvent(response.body()!!.data!!.toEntity(version))
            }
        }
    }

    // -------- [ Bulk fetching ] --------

    override suspend fun fetchEvents(version: String) {
        val response = gameApi.getAllEvents()
        if (response.isSuccessful) {
            gameDatabase.withTransaction {
                gameDatabase.eventDao.upsertAllEvents(response.body()!!.data!!.map {
                    it.toEntity(version)
                })
            }
        }
    }
}