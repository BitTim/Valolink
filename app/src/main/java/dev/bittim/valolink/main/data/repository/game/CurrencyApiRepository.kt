package dev.bittim.valolink.main.data.repository.game

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.main.data.local.game.GameDatabase
import dev.bittim.valolink.main.data.remote.game.GameApi
import dev.bittim.valolink.main.data.worker.game.AgentSyncWorker
import dev.bittim.valolink.main.data.worker.game.CurrencySyncWorker
import dev.bittim.valolink.main.domain.model.game.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
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
        providedVersion: String?,
    ): Flow<Currency> {
        return gameDatabase.currencyDao.getByUuid(uuid).distinctUntilChanged().combineTransform(
            versionRepository.get()
        ) { entity, apiVersion ->
            val version = providedVersion ?: apiVersion.version

            if (entity == null || entity.version != version) {
                queueWorker(
                    uuid,
                    version
                )
            } else {
                emit(entity)
            }
        }.map { it.toType() }
    }

    // -------- [ Bulk queries ] --------

    override suspend fun getAll(
        providedVersion: String?,
    ): Flow<List<Currency>> {
        return gameDatabase.currencyDao.getAll().distinctUntilChanged().combineTransform(
            versionRepository.get()
        ) { currencies, apiVersion ->
            val version = providedVersion ?: apiVersion.version

            if (currencies.isEmpty() || currencies.any { it.version != version }) {
                queueWorker(version)
            } else {
                emit(currencies)
            }
        }.map { currencies -> currencies.map { it.toType() } }
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

    override fun queueWorker(version: String, uuid: String?) {
        val workRequest = OneTimeWorkRequestBuilder<CurrencySyncWorker>()
            .setInputData(
                workDataOf(
                    CurrencySyncWorker.KEY_CURRENCY_UUID to uuid,
                    CurrencySyncWorker.KEY_VERSION to version
                )
            )
            .setConstraints(Constraints(NetworkType.CONNECTED))
            .build()
        workManager.enqueueUniqueWork(
            CurrencySyncWorker.WORK_NAME,
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            workRequest
        )
    }
}