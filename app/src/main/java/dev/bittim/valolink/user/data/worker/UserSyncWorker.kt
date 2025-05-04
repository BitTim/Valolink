/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserSyncWorker.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   04.05.25, 14:03
 */

package dev.bittim.valolink.user.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.room.withTransaction
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.bittim.valolink.user.data.local.UserDatabase
import dev.bittim.valolink.user.data.repository.SessionRepository
import dev.bittim.valolink.user.data.repository.data.UserAgentRepository
import dev.bittim.valolink.user.data.repository.data.UserContractRepository
import dev.bittim.valolink.user.data.repository.data.UserLevelRepository
import dev.bittim.valolink.user.data.repository.data.UserMetaRepository
import dev.bittim.valolink.user.data.repository.data.UserRankRepository
import kotlinx.coroutines.flow.firstOrNull
import java.time.OffsetDateTime
import kotlin.coroutines.cancellation.CancellationException

@HiltWorker
class UserSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val userDatabase: UserDatabase,
    private val userMetaRepository: UserMetaRepository,
    private val userAgentRepository: UserAgentRepository,
    private val userContractRepository: UserContractRepository,
    private val userLevelRepository: UserLevelRepository,
    private val userRankRepository: UserRankRepository,
    private val sessionRepository: SessionRepository,
) : CoroutineWorker(
    context, params
) {
    override suspend fun doWork(): Result {
        // Check if user is authenticated
        if (sessionRepository.isAuthenticated().firstOrNull() != true) return Result.failure()

        // Check if user is in local mode
        if (sessionRepository.isLocal().firstOrNull() == true) return Result.success()

        // Retrieve contract uuid from input data
        val type = inputData.getString(KEY_TYPE) ?: return Result.failure()
        val relation = inputData.getString(KEY_RELATION) ?: return Result.failure()

        // Get corresponding repository
        val repository = when (type) {
            "UserData" -> userMetaRepository
            "UserAgent" -> userAgentRepository
            "UserContract" -> userContractRepository
            "UserLevel" -> userLevelRepository
            "UserRank" -> userRankRepository
            else -> return Result.failure()
        }

        // Get most recent data from local database and Supabase
        val remoteData = repository.remoteQuery(relation) ?: return Result.failure()
        val dirtyLocalData = userDatabase.getByRelation(type, relation).firstOrNull()

        // Clean up local database by deleting non existing remote entries
        dirtyLocalData?.forEach { local ->
            val remote = remoteData.find {
                it.getIdentifier() == local.getIdentifier()
            }

            if (remote == null) {
                if (local.isSynced) userDatabase.deleteByUuid(type, local.uuid)
            } else {
                userDatabase.withTransaction {
                    if (local.uuid != remote.uuid) userDatabase.deleteByUuid(type, local.uuid)
                }
            }
        }

        // Get cleaned local data for further use
        val localData = userDatabase.getByRelation(type, relation).firstOrNull()

        // Upsert Supabase data only, if it is more recent
        try {
            remoteData.forEach { remote ->
                val local = localData?.find {
                    it.getIdentifier() == remote.getIdentifier()
                }

                if (local == null || OffsetDateTime.parse(local.updatedAt)
                        .isBefore(OffsetDateTime.parse(remote.updatedAt))
                ) {
                    userDatabase.upsert(type, remote.toEntity(isSynced = true, toDelete = false))
                }
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
        }

        // Push write queue to Supabase
        val syncQueue = userDatabase.getSyncQueue(type).firstOrNull()
        syncQueue?.forEach { entity ->
            if (entity == null) return@forEach

            if (entity.toDelete) {
                if (repository.remoteDelete(entity.uuid)) {
                    userDatabase.deleteByUuid(type, entity.uuid)
                }
            } else {
                if (repository.remoteUpsert(entity.uuid)) {
                    userDatabase.upsert(type, entity.withIsSynced(true))
                }
            }
        }

        return Result.success()
    }


    companion object {
        const val WORK_BASE_NAME = "UserSyncWorker"
        const val KEY_TYPE = "KEY_TYPE"
        const val KEY_RELATION = "KEY_RELATION"
    }
}
