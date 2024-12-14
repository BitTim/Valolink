/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       UserContractSupabaseRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.user.data.repository.data

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.user.data.local.UserDatabase
import dev.bittim.valolink.user.data.local.entity.UserContractEntity
import dev.bittim.valolink.user.data.remote.dto.UserContractDto
import dev.bittim.valolink.user.data.repository.SessionRepository
import dev.bittim.valolink.user.data.worker.UserSyncWorker
import dev.bittim.valolink.user.domain.model.UserContract
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class UserContractSupabaseRepository @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userLevelRepository: UserLevelRepository,
    private val userDatabase: UserDatabase,
    private val database: Postgrest,
    private val workManager: WorkManager,
) : UserContractRepository {
    companion object {
        const val TABLE_NAME = "contracts"
    }

    // ================================
    //  Get
    // ================================

    override suspend fun getAllWithCurrentUser(): Flow<List<UserContract>> {
        val uid = sessionRepository.getUid() ?: return flow { }
        return getAll(uid)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getAll(uid: String): Flow<List<UserContract>> {
        return try {
            // Get from local database
            val contractsFlow = userDatabase.userContractDao.getByUser(uid).distinctUntilChanged()
                .flatMapLatest { contracts ->
                    combine(contracts.map { contract ->
                        combine(
                            flowOf(contract), userLevelRepository.getAll(contract.uuid)
                        ) { c, levels ->
                            c.toType(levels)
                        }
                    }) { it.toList() }
                }

            // Queue worker to sync with Supabase
            queueWorker(uid)

            // Return
            contractsFlow
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return flow { }
        }
    }

    override suspend fun getWithCurrentUser(uuid: String): Flow<UserContract?> {
        val uid = sessionRepository.getUid() ?: return flow { }
        return get(uid, uuid)
    }

    override suspend fun get(
        uid: String,
        uuid: String,
    ): Flow<UserContract?> {
        return try {
            // Get from local database
            val contractFlow = userDatabase.userContractDao.getByUserAndContract(
                uid, uuid
            ).distinctUntilChanged()
            val levelsFlow = userLevelRepository.getAll(uuid)

            val combinedFlow = combine(contractFlow, levelsFlow) { contract, levels ->
                contract?.toType(levels)
            }

            // Queue worker to sync with Supabase
            queueWorker(uid)

            // Return
            combinedFlow
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
        obj: UserContract,
        toDelete: Boolean,
    ): Boolean {
        return try {
            // Add to local database
            userDatabase.userContractDao.upsert(
                UserContractEntity.fromType(obj, false, toDelete)
            )
            obj.levels.forEach { userLevelRepository.set(it, toDelete) }

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

    override suspend fun set(obj: UserContract): Boolean {
        return set(obj, false)
    }

    override suspend fun delete(obj: UserContract?): Boolean {
        if (obj == null) return false
        return set(obj, true)
    }

    // ================================
    //  Remote operations
    // ================================

    override suspend fun remoteQuery(relation: String): List<UserContractDto>? {
        try {
            return database.from(TABLE_NAME).select {
                filter { UserContractDto::user eq relation }
            }.decodeList<UserContractDto>()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return null
        }
    }

    override suspend fun remoteUpsert(uuid: String): Boolean {
        return try {
            val entity = userDatabase.userContractDao.getByUuid(uuid).firstOrNull() ?: return false
            val dto = UserContractDto.fromEntity(entity)

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
                filter { UserContractDto::uuid eq uuid }
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
                UserSyncWorker.KEY_TYPE to UserContract::class.simpleName,
                UserSyncWorker.KEY_RELATION to uid
            )
        ).setConstraints(Constraints(NetworkType.CONNECTED)).build()

        workManager.enqueueUniqueWork(
            UserContract::class.simpleName + UserSyncWorker.WORK_BASE_NAME,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
}