package dev.bittim.valolink.main.data.repository.game

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.main.data.local.game.GameDatabase
import dev.bittim.valolink.main.data.remote.game.GameApi
import dev.bittim.valolink.main.data.worker.game.GameSyncWorker
import dev.bittim.valolink.main.domain.model.game.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CurrencyApiRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi,
    private val versionRepository: VersionRepository,
    private val workManager: WorkManager,
) : CurrencyRepository {
    // --------------------------------
    //  Query from Database
    // --------------------------------

    // -------- [ Single queries ] --------

    override suspend fun getByUuid(
        uuid: String,
    ): Flow<Currency?> {
        return try {
            // Get from local database
            val local = gameDatabase.currencyDao
                .getByUuid(uuid)
                .distinctUntilChanged()
                .map { it?.toType() }

            // Queue worker to fetch newest data from API
            //  -> Worker will check if fetch is needed itself
            queueWorker(uuid)

            // Return
            local
        } catch (e: Exception) {
            e.printStackTrace()
            return flow { }
        }
    }

    // -------- [ Bulk queries ] --------

    override suspend fun getAll(
    ): Flow<List<Currency>> {
        return try {
            // Get from local database
            val local = gameDatabase.currencyDao
                .getAll()
                .distinctUntilChanged()
                .map { entities ->
                    entities.map { it.toType() }
                }

            // Queue worker to fetch newest data from API
            //  -> Worker will check if fetch is needed itself
            queueWorker()

            // Return
            local
        } catch (e: Exception) {
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
        val response = gameApi.getCurrency(uuid)
        if (response.isSuccessful) {
            gameDatabase.currencyDao.upsert(response.body()!!.data!!.toEntity(version))
        }
    }

    // -------- [ Bulk fetching ] --------

    override suspend fun fetchAll(
        version: String,
    ) {
        val response = gameApi.getAllCurrencies()
        if (response.isSuccessful) {
            gameDatabase.currencyDao.upsert(
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
        val workRequest = OneTimeWorkRequestBuilder<GameSyncWorker>()
            .setInputData(
                workDataOf(
                    GameSyncWorker.KEY_TYPE to Currency::class.simpleName,
                    GameSyncWorker.KEY_UUID to uuid,
                )
            )
            .setConstraints(Constraints(NetworkType.CONNECTED))
            .build()
        workManager.enqueueUniqueWork(
            Currency::class.simpleName + GameSyncWorker.WORK_BASE_NAME + if (!uuid.isNullOrEmpty()) "_$uuid" else "",
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }
}