package dev.bittim.valolink.main.data.repository.game

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.main.data.local.game.GameDatabase
import dev.bittim.valolink.main.data.remote.game.GameApi
import dev.bittim.valolink.main.data.worker.game.PlayerCardSyncWorker
import dev.bittim.valolink.main.domain.model.game.PlayerCard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayerCardApiRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi,
    private val versionRepository: VersionRepository,
    private val workManager: WorkManager
) : PlayerCardRepository {
    // --------------------------------
    //  Query from Database
    // --------------------------------

    // -------- [ Single queries ] --------

    override suspend fun getByUuid(
        uuid: String,
        providedVersion: String?,
    ): Flow<PlayerCard> {
        return gameDatabase.playerCardDao
            .getByUuid(uuid)
            .distinctUntilChanged()
            .combineTransform(
                versionRepository.get()
            ) { entity, apiVersion ->
                val version = providedVersion ?: apiVersion.version

                if (entity == null || entity.version != version) {
                    queueWorker(
                        version,
                        uuid
                    )
                } else {
                    emit(entity)
                }
            }
            .map { it.toType() }
    }

    // -------- [ Bulk queries ] --------

    override suspend fun getAll(
        providedVersion: String?,
    ): Flow<List<PlayerCard>> {
        return gameDatabase.playerCardDao
            .getAll()
            .distinctUntilChanged()
            .combineTransform(
                versionRepository.get()
            ) { playerCards, apiVersion ->
                val version = providedVersion ?: apiVersion.version

                if (playerCards.isEmpty() || playerCards.any { it.version != version }) {
                    queueWorker(version)
                } else {
                    emit(playerCards)
                }
            }
            .map { playerCards -> playerCards.map { it.toType() } }
    }

    // --------------------------------
    //  Fetching from API
    // --------------------------------

    // -------- [ Single fetching ] --------

    override suspend fun fetch(
        uuid: String,
        version: String,
    ) {
        val response = gameApi.getPlayerCard(uuid)
        if (response.isSuccessful) {
            gameDatabase.playerCardDao.upsert(response.body()!!.data!!.toEntity(version))
        }
    }

    // -------- [ Bulk fetching ] --------

    override suspend fun fetchAll(
        version: String,
    ) {
        val response = gameApi.getAllPlayerCards()
        if (response.isSuccessful) {
            gameDatabase.playerCardDao.upsert(
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
        val workRequest = OneTimeWorkRequestBuilder<PlayerCardSyncWorker>()
            .setInputData(
                workDataOf(
                    PlayerCardSyncWorker.KEY_PLAYERCARD_UUID to uuid,
                    PlayerCardSyncWorker.KEY_VERSION to version
                )
            )
            .setConstraints(Constraints(NetworkType.CONNECTED))
            .build()
        workManager.enqueueUniqueWork(
            PlayerCardSyncWorker.WORK_NAME + if (!uuid.isNullOrEmpty()) "_$uuid" else "",
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }
}