/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       UserAgentSupabaseRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.user.data.repository.data

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.user.data.local.UserDatabase
import dev.bittim.valolink.user.data.local.entity.UserAgentEntity
import dev.bittim.valolink.user.data.remote.dto.UserAgentDto
import dev.bittim.valolink.user.data.repository.SessionRepository
import dev.bittim.valolink.user.data.worker.UserSyncWorker
import dev.bittim.valolink.user.domain.model.UserAgent
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class UserAgentSupabaseRepository @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userDatabase: UserDatabase,
    private val database: Postgrest,
    private val workManager: WorkManager,
) : UserAgentRepository {
    companion object {
        const val TABLE_NAME = "agents"
    }

    // ================================
    //  Get
    // ================================

    override suspend fun getAllWithCurrentUser(): Flow<List<UserAgent>> {
        val uid = sessionRepository.getUid() ?: return flow { }
        return getAll(uid)
    }

    override suspend fun getAll(uid: String): Flow<List<UserAgent>> {
        return try {
            // Get from local database
            val agentsFlow = userDatabase.userAgentDao.getByUser(uid).distinctUntilChanged()
                .map { agents -> agents.map { it.toType() } }

            // Queue worker to sync with Supabase
            queueWorker(uid)

            // Return
            agentsFlow
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return flow { }
        }
    }

    override suspend fun getWithCurrentUser(uuid: String): Flow<UserAgent?> {
        val uid = sessionRepository.getUid() ?: return flow { }
        return get(uid, uuid)
    }

    override suspend fun get(
        uid: String,
        uuid: String,
    ): Flow<UserAgent?> {
        return try {
            // Get from local database
            val agentFlow =
                userDatabase.userAgentDao.getByUserAndAgent(uid, uuid).distinctUntilChanged()
                    .map { it?.toType() }

            // Queue worker to sync with Supabase
            queueWorker(uid)

            // Return
            agentFlow
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return flow { }
        }
    }

    // ================================
    //  Set
    // ================================

    override suspend fun set(
        obj: UserAgent,
        toDelete: Boolean,
    ): Boolean {
        return try {
            // Add to local database
            userDatabase.userAgentDao.upsert(
                UserAgentEntity.fromType(obj, false, toDelete)
            )

            // Queue worker to sync with Supabase
            queueWorker(obj.user)

            // Return
            true
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            false
        }
    }

    override suspend fun set(obj: UserAgent): Boolean {
        return set(obj, false)
    }

    override suspend fun delete(obj: UserAgent): Boolean {
        return set(obj, true)
    }

    // ================================
    //  Remote operations
    // ================================

    override suspend fun remoteQuery(relation: String): List<UserAgentDto>? {
        try {
            return database.from(TABLE_NAME).select {
                filter { UserAgentDto::user eq relation }
            }.decodeList<UserAgentDto>()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return null
        }
    }

    override suspend fun remoteUpsert(uuid: String): Boolean {
        return try {
            val entity = userDatabase.userAgentDao.getByUuid(uuid).firstOrNull() ?: return false
            val dto = UserAgentDto.fromEntity(entity)

            database.from(TABLE_NAME).upsert(dto)
            true
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            false
        }
    }

    override suspend fun remoteDelete(uuid: String): Boolean {
        return try {
            database.from(TABLE_NAME).delete {
                filter { UserAgentDto::uuid eq uuid }
            }
            true
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            false
        }
    }

    // ================================
    //  Worker
    // ================================

    override fun queueWorker(uid: String) {
        val workRequest = OneTimeWorkRequestBuilder<UserSyncWorker>().setInputData(
            workDataOf(
                UserSyncWorker.KEY_TYPE to UserAgent::class.simpleName,
                UserSyncWorker.KEY_RELATION to uid
            )
        ).setConstraints(Constraints(NetworkType.CONNECTED)).build()

        workManager.enqueueUniqueWork(
            UserAgent::class.simpleName + UserSyncWorker.WORK_BASE_NAME,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
}