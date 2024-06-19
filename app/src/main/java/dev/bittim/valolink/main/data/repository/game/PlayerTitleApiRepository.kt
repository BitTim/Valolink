package dev.bittim.valolink.main.data.repository.game

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.main.data.local.game.GameDatabase
import dev.bittim.valolink.main.data.remote.game.GameApi
import dev.bittim.valolink.main.data.worker.game.PlayerTitleSyncWorker
import dev.bittim.valolink.main.domain.model.game.PlayerTitle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayerTitleApiRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi,
    private val versionRepository: VersionRepository,
    private val workManager: WorkManager,
) : PlayerTitleRepository {
    // --------------------------------
    //  Query from Database
    // --------------------------------

    // -------- [ Single queries ] --------

    override suspend fun getByUuid(
        uuid: String,
        providedVersion: String?,
    ): Flow<PlayerTitle> {
        return gameDatabase.playerTitleDao
            .getByUuid(uuid)
            .distinctUntilChanged()
            .combineTransform(
                versionRepository.get()
            ) { entity, apiVersion ->
                val version = providedVersion ?: apiVersion.version

                if (entity == null || entity.version != version) {
                    queueWorker(uuid)
                } else {
                    emit(entity)
                }
            }
            .map { it.toType() }
    }

    // -------- [ Bulk queries ] --------

    override suspend fun getAll(
        providedVersion: String?,
    ): Flow<List<PlayerTitle>> {
        return gameDatabase.playerTitleDao
            .getAll()
            .distinctUntilChanged()
            .combineTransform(
                versionRepository.get()
            ) { playerTitles, apiVersion ->
                val version = providedVersion ?: apiVersion.version

                if (playerTitles.isEmpty() || playerTitles.any { it.version != version }) {
                    queueWorker()
                } else {
                    emit(playerTitles)
                }
            }
            .map { playerTitles -> playerTitles.map { it.toType() } }
    }

    // --------------------------------
    //  Fetching from API
    // --------------------------------

    // -------- [ Single fetching ] --------

    override suspend fun fetch(
        uuid: String,
        version: String,
    ) {
        val response = gameApi.getPlayerTitle(uuid)
        if (response.isSuccessful) {
            gameDatabase.playerTitleDao.upsert(response.body()!!.data!!.toEntity(version))
        }
    }

    // -------- [ Bulk fetching ] --------

    override suspend fun fetchAll(
        version: String,
    ) {
        val response = gameApi.getAllPlayerTitles()
        if (response.isSuccessful) {
            gameDatabase.playerTitleDao.upsert(
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
        val workRequest = OneTimeWorkRequestBuilder<PlayerTitleSyncWorker>()
            .setInputData(
                workDataOf(
                    PlayerTitleSyncWorker.KEY_UUID to uuid,
                )
            )
            .setConstraints(Constraints(NetworkType.CONNECTED))
            .build()
        workManager.enqueueUniqueWork(
            PlayerTitleSyncWorker.WORK_NAME + if (!uuid.isNullOrEmpty()) "_$uuid" else "",
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }
}