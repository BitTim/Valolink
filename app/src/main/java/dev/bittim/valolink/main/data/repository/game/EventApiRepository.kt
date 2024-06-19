package dev.bittim.valolink.main.data.repository.game

import androidx.room.withTransaction
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.main.data.local.game.GameDatabase
import dev.bittim.valolink.main.data.remote.game.GameApi
import dev.bittim.valolink.main.data.worker.game.EventSyncWorker
import dev.bittim.valolink.main.domain.model.game.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventApiRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi,
    private val versionRepository: VersionRepository,
    private val workManager: WorkManager,
) : EventRepository {
    // --------------------------------
    //  Query from Database
    // --------------------------------

    // -------- [ Single queries ] --------

    override suspend fun getByUuid(
        uuid: String,
        providedVersion: String?,
    ): Flow<Event> {
        return gameDatabase.eventDao.getByUuid(uuid).distinctUntilChanged().combineTransform(
            versionRepository.get()
        ) { event, apiVersion ->
            val version = providedVersion ?: apiVersion.version

            if (event == null || event.version != version) {
                queueWorker(uuid)
            } else {
                emit(event)
            }
        }.map { it.toType() }
    }

    // -------- [ Bulk queries ] --------

    override suspend fun getAll(providedVersion: String?): Flow<List<Event>> {
        return gameDatabase.eventDao.getAll().distinctUntilChanged().combineTransform(
            versionRepository.get()
        ) { events, apiVersion ->
            val version = providedVersion ?: apiVersion.version

            if (events.isEmpty() || events.any { it.version != version }) {
                queueWorker()
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

    override suspend fun fetch(
        uuid: String,
        version: String,
    ) {
        val response = gameApi.getEvent(uuid)
        if (response.isSuccessful) {
            gameDatabase.withTransaction {
                gameDatabase.eventDao.upsert(response.body()!!.data!!.toEntity(version))
            }
        }
    }

    // -------- [ Bulk fetching ] --------

    override suspend fun fetchAll(version: String) {
        val response = gameApi.getAllEvents()
        if (response.isSuccessful) {
            gameDatabase.withTransaction {
                gameDatabase.eventDao.upsert(response.body()!!.data!!.map {
                    it.toEntity(version)
                }.distinct().toSet())
            }
        }
    }

    // ================================
    //  Queue Worker
    // ================================

    override fun queueWorker(
        uuid: String?,
    ) {
        val workRequest = OneTimeWorkRequestBuilder<EventSyncWorker>()
            .setInputData(
                workDataOf(
                    EventSyncWorker.KEY_UUID to uuid,
                )
            )
            .setConstraints(Constraints(NetworkType.CONNECTED))
            .build()
        workManager.enqueueUniqueWork(
            EventSyncWorker.WORK_NAME + if (!uuid.isNullOrEmpty()) "_$uuid" else "",
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }
}