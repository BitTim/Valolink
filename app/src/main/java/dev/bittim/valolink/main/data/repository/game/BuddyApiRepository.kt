package dev.bittim.valolink.main.data.repository.game

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.main.data.local.game.GameDatabase
import dev.bittim.valolink.main.data.remote.game.GameApi
import dev.bittim.valolink.main.data.worker.game.BuddySyncWorker
import dev.bittim.valolink.main.domain.model.game.buddy.Buddy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BuddyApiRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi,
    private val versionRepository: VersionRepository,
    private val workManager: WorkManager,
) : BuddyRepository {
    // --------------------------------
    //  Query from Database
    // --------------------------------

    // -------- [ Single queries ] --------

    override suspend fun getByUuid(
        uuid: String,
        providedVersion: String?,
    ): Flow<Buddy> {
        return gameDatabase.buddyDao.getByUuid(uuid).distinctUntilChanged().combineTransform(
            versionRepository.get()
        ) { entity, apiVersion ->
            val version = providedVersion ?: apiVersion.version

            if (entity == null || entity.buddy.version != version) {
                queueWorker(uuid)
            } else {
                emit(entity)
            }
        }.map { it.toType() }
    }

    override suspend fun getByLevelUuid(
        levelUuid: String,
        providedVersion: String?,
    ): Flow<Buddy> {
        return gameDatabase.buddyDao
            .getByLevelUuid(levelUuid)
            .distinctUntilChanged()
            .combineTransform(
                versionRepository.get()
            ) { entity, apiVersion ->
                val version = providedVersion ?: apiVersion.version

                if (entity == null || entity.buddy.version != version) {
                    queueWorker()
                } else {
                    emit(entity)
                }
            }
            .map { it.toType() }
    }

    // -------- [ Bulk queries ] --------

    override suspend fun getAll(providedVersion: String?): Flow<List<Buddy>> {
        return gameDatabase.buddyDao.getAll().distinctUntilChanged().combineTransform(
            versionRepository.get()
        ) { entities, apiVersion ->
            val version = providedVersion ?: apiVersion.version

            if (entities.isEmpty() || entities.any { it.buddy.version != version }) {
                queueWorker()
            } else {
                emit(entities.map { it.toType() })
            }
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
        val response = gameApi.getBuddy(uuid)
        if (response.isSuccessful) {
            val buddyDto = response.body()!!.data!!

            val buddy = buddyDto.toEntity(version)
            val levels = buddyDto.levels.map {
                it.toEntity(
                    version,
                    buddy.uuid
                )
            }

            gameDatabase.buddyDao.upsert(
                buddy,
                levels.distinct().toSet()
            )
        }
    }

    // -------- [ Bulk fetching ] --------

    override suspend fun fetchAll(version: String) {
        val response = gameApi.getAllBuddies()
        if (response.isSuccessful) {
            val buddyDto = response.body()!!.data!!

            val buddies = buddyDto.map { it.toEntity(version) }
            val levels = buddyDto.zip(buddies) { level, buddy ->
                level.levels.map {
                    it.toEntity(
                        version,
                        buddy.uuid
                    )
                }
            }.flatten()

            gameDatabase.buddyDao.upsert(
                buddies.distinct().toSet(),
                levels.distinct().toSet()
            )
        }
    }

    // ================================
    //  Queue Worker
    // ================================

    override fun queueWorker(
        uuid: String?,
    ) {
        val workRequest = OneTimeWorkRequestBuilder<BuddySyncWorker>()
            .setInputData(
                workDataOf(
                    BuddySyncWorker.KEY_UUID to uuid,
                )
            )
            .setConstraints(Constraints(NetworkType.CONNECTED))
            .build()
        workManager.enqueueUniqueWork(
            BuddySyncWorker.WORK_NAME + if (!uuid.isNullOrEmpty()) "_$uuid" else "",
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }
}