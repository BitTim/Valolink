package dev.bittim.valolink.core.data.repository.game

import androidx.room.withTransaction
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.core.data.local.game.GameDatabase
import dev.bittim.valolink.core.data.remote.game.GameApi
import dev.bittim.valolink.core.data.worker.game.GameSyncWorker
import dev.bittim.valolink.main.domain.model.game.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class EventApiRepository @Inject constructor(
	private val gameDatabase: GameDatabase,
	private val gameApi: GameApi,
	private val workManager: WorkManager,
) : EventRepository {
	// --------------------------------
	//  Query from Database
	// --------------------------------

	// -------- [ Single queries ] --------

	override suspend fun getByUuid(
		uuid: String,
	): Flow<Event?> {
		return try {
			// Get from local database
			val local =
				gameDatabase.eventDao.getByUuid(uuid).distinctUntilChanged().map { it?.toType() }

			// Queue worker to fetch newest data from API
			//  -> Worker will check if fetch is needed itself
			queueWorker(uuid)

			// Return
			local
		} catch (e: Exception) {
			if (e is CancellationException) throw e
			e.printStackTrace()
			return flow { }
		}
	}

	// -------- [ Bulk queries ] --------

	override suspend fun getAll(): Flow<List<Event>> {
		return try {
			// Get from local database
			val local = gameDatabase.eventDao.getAll().distinctUntilChanged().map { entities ->
				entities.map { it.toType() }
			}

			// Queue worker to fetch newest data from API
			//  -> Worker will check if fetch is needed itself
			queueWorker()

			// Return
			local
		} catch (e: Exception) {
			if (e is CancellationException) throw e
			e.printStackTrace()
			return flow { }
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
		val workRequest = OneTimeWorkRequestBuilder<GameSyncWorker>().setInputData(
			workDataOf(
				GameSyncWorker.KEY_TYPE to Event::class.simpleName,
				GameSyncWorker.KEY_UUID to uuid,
			)
		).setConstraints(Constraints(NetworkType.CONNECTED)).build()
		workManager.enqueueUniqueWork(
			Event::class.simpleName + GameSyncWorker.WORK_BASE_NAME + if (!uuid.isNullOrEmpty()) "_$uuid" else "",
			ExistingWorkPolicy.KEEP,
			workRequest
		)
	}
}