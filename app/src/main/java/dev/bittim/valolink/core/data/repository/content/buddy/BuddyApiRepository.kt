package dev.bittim.valolink.core.data.repository.content.buddy

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.core.data.local.content.ContentDatabase
import dev.bittim.valolink.core.data.remote.content.ContentApi
import dev.bittim.valolink.core.data.worker.content.ContentSyncWorker
import dev.bittim.valolink.main.domain.model.game.buddy.Buddy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class BuddyApiRepository @Inject constructor(
	private val contentDatabase: ContentDatabase,
	private val contentApi: ContentApi,
	private val workManager: WorkManager,
) : BuddyRepository {
	// --------------------------------
	//  Query from Database
	// --------------------------------

	// -------- [ Single queries ] --------

	override suspend fun getByUuid(
		uuid: String,
	): Flow<Buddy?> {
		return try {
			// Get from local database
			val local =
				contentDatabase.buddyDao.getByUuid(uuid).distinctUntilChanged().map { it?.toType() }

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

	override suspend fun getByLevelUuid(
		levelUuid: String,
	): Flow<Buddy?> {
		return try {
			// Get from local database
			val local = contentDatabase.buddyDao.getByLevelUuid(levelUuid).distinctUntilChanged()
				.map { it?.toType() }

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

	// -------- [ Bulk queries ] --------

	override suspend fun getAll(): Flow<List<Buddy>> {
		return try {
			// Get from local database
			val local = contentDatabase.buddyDao.getAll().distinctUntilChanged()
				.map { entities -> entities.map { it.toType() } }

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
		val response = contentApi.getBuddy(uuid)
		if (response.isSuccessful) {
			val buddyDto = response.body()!!.data!!

			val buddy = buddyDto.toEntity(version)
			val levels = buddyDto.levels.map {
				it.toEntity(
					version, buddy.uuid
				)
			}

			contentDatabase.buddyDao.upsert(
				buddy, levels.distinct().toSet()
			)
		}
	}

	// -------- [ Bulk fetching ] --------

	override suspend fun fetchAll(version: String) {
		val response = contentApi.getAllBuddies()
		if (response.isSuccessful) {
			val buddyDto = response.body()!!.data!!

			val buddies = buddyDto.map { it.toEntity(version) }
			val levels = buddyDto.zip(buddies) { level, buddy ->
				level.levels.map {
					it.toEntity(
						version, buddy.uuid
					)
				}
			}.flatten()

			contentDatabase.buddyDao.upsert(
				buddies.distinct().toSet(), levels.distinct().toSet()
			)
		}
	}

	// ================================
	//  Queue Worker
	// ================================

	override fun queueWorker(
		uuid: String?,
	) {
		val workRequest = OneTimeWorkRequestBuilder<ContentSyncWorker>().setInputData(
			workDataOf(
				ContentSyncWorker.KEY_TYPE to Buddy::class.simpleName,
				ContentSyncWorker.KEY_UUID to uuid,
			)
		).setConstraints(Constraints(NetworkType.CONNECTED)).build()
		workManager.enqueueUniqueWork(
			Buddy::class.simpleName + ContentSyncWorker.WORK_BASE_NAME + if (!uuid.isNullOrEmpty()) "_$uuid" else "",
			ExistingWorkPolicy.KEEP,
			workRequest
		)
	}
}