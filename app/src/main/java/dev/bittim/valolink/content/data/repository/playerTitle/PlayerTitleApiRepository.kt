package dev.bittim.valolink.content.data.repository.playerTitle

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.content.data.local.ContentDatabase
import dev.bittim.valolink.content.data.remote.ContentApi
import dev.bittim.valolink.content.data.worker.ContentSyncWorker
import dev.bittim.valolink.content.domain.model.PlayerTitle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class PlayerTitleApiRepository @Inject constructor(
	private val contentDatabase: ContentDatabase,
	private val contentApi: ContentApi,
	private val workManager: WorkManager,
) : PlayerTitleRepository {
	// --------------------------------
	//  Query from Database
	// --------------------------------

	// -------- [ Single queries ] --------

	override suspend fun getByUuid(
		uuid: String,
	): Flow<PlayerTitle?> {
		return try {
			// Get from local database
			val local = contentDatabase.playerTitleDao.getByUuid(uuid).distinctUntilChanged()
				.map { it?.toType() }

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

	override suspend fun getAll(
	): Flow<List<PlayerTitle>> {
		return try {
			// Get from local database
			val local =
				contentDatabase.playerTitleDao.getAll().distinctUntilChanged().map { entities ->
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
		val response = contentApi.getPlayerTitle(uuid)
		if (response.isSuccessful) {
			contentDatabase.playerTitleDao.upsert(response.body()!!.data!!.toEntity(version))
		}
	}

	// -------- [ Bulk fetching ] --------

	override suspend fun fetchAll(
		version: String,
	) {
		val response = contentApi.getAllPlayerTitles()
		if (response.isSuccessful) {
			contentDatabase.playerTitleDao.upsert(
				response.body()!!.data!!.map {
					it.toEntity(version)
				}.distinct().toSet()
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
				ContentSyncWorker.KEY_TYPE to PlayerTitle::class.simpleName,
				ContentSyncWorker.KEY_UUID to uuid,
			)
		).setConstraints(Constraints(NetworkType.CONNECTED)).build()
		workManager.enqueueUniqueWork(
			PlayerTitle::class.simpleName + ContentSyncWorker.WORK_BASE_NAME + if (!uuid.isNullOrEmpty()) "_$uuid" else "",
			ExistingWorkPolicy.KEEP,
			workRequest
		)
	}
}