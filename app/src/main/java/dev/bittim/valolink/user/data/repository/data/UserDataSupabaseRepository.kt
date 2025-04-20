/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserDataSupabaseRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   20.04.25, 03:29
 */

package dev.bittim.valolink.user.data.repository.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.onboarding.ui.screens.OnboardingScreen
import dev.bittim.valolink.user.data.local.UserDatabase
import dev.bittim.valolink.user.data.local.entity.UserDataEntity
import dev.bittim.valolink.user.data.remote.dto.UserDataDto
import dev.bittim.valolink.user.data.repository.SessionRepository
import dev.bittim.valolink.user.data.worker.UserSyncWorker
import dev.bittim.valolink.user.domain.model.UserData
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class UserDataSupabaseRepository @Inject constructor(
    private val context: Context,
    private val sessionRepository: SessionRepository,
    private val userAgentRepository: UserAgentRepository,
    private val userContractRepository: UserContractRepository,
    private val userRankRepository: UserRankRepository,
    private val userDatabase: UserDatabase,
    private val database: Postgrest,
    private val storage: Storage,
    private val workManager: WorkManager,
) : UserDataRepository {
    companion object {
        const val TABLE_NAME = "users"
    }

    // ================================
    //  Get User Data
    // ================================

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getWithCurrentUser(): Flow<UserData?> {
        return sessionRepository.getUid().flatMapLatest { uid ->
            if (uid == null) {
                flowOf(null)
            } else get(uid)
        }
    }

    override fun get(uid: String): Flow<UserData?> {
        return try {
            // Get from local database
            val userFlow = userDatabase.userDataDao.getByUuid(uid).distinctUntilChanged()
            val agentsFlow = userAgentRepository.getAll(uid)
            val contractsFlow = userContractRepository.getAll(uid)
            val rankFlow = userRankRepository.get(uid)

            val combinedFlow = combine(
                userFlow, agentsFlow, contractsFlow, rankFlow
            ) { user, agents, contracts, rank ->
                user?.toType(agents, contracts, rank)
            }

            // Queue worker to sync with Supabase
            queueWorker(uid)

            // Return
            combinedFlow
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return flow { emit(null) }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun hasOnboardedWithCurrentUser(): Flow<Boolean?> {
        return sessionRepository.getUid().flatMapLatest { uid ->
            if (uid == null) {
                flow {
                    emit(null)
                }
            } else hasOnboarded(uid)
        }
    }

    override fun hasOnboarded(uid: String): Flow<Boolean?> {
        return get(uid).map { data ->
            data?.let { it.onboardingStep + OnboardingScreen.STEP_OFFSET > OnboardingScreen.getMaxStep() }
        }
    }


    // ================================
    //  Set User Data
    // ================================

    override suspend fun createEmptyForCurrentUser(): Boolean {
        val uid = sessionRepository.getUid().firstOrNull() ?: return false
        val data = UserData.empty(uid)

        return set(uid, data)
    }

    override suspend fun setWithCurrentUser(userData: UserData, toDelete: Boolean): Boolean {
        val uid = sessionRepository.getUid().firstOrNull() ?: return false
        return set(uid, userData, toDelete)
    }

    override suspend fun set(
        uid: String,
        userData: UserData,
        toDelete: Boolean,
    ): Boolean {
        return try {
            // Insert into local database
            userDatabase.userDataDao.upsert(
                UserDataEntity.fromType(userData, false, toDelete)
            )
            userData.agents.forEach { userAgentRepository.set(it, toDelete) }
            userData.contracts.forEach { userContractRepository.set(it, toDelete) }
            if (userData.rank != null) userRankRepository.set(userData.rank, toDelete)

            // Queue worker to sync with Supabase
            queueWorker(uid)

            // Return
            true
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            false
        }
    }

    override suspend fun setWithCurrentUser(userData: UserData): Boolean {
        val uid = sessionRepository.getUid().firstOrNull() ?: return false
        return set(uid, userData)
    }

    override suspend fun set(uid: String, userData: UserData): Boolean {
        return set(uid, userData, false)
    }

    override suspend fun deleteWithCurrentUser(userData: UserData): Boolean {
        val uid = sessionRepository.getUid().firstOrNull() ?: return false
        return delete(uid, userData)
    }

    override suspend fun delete(uid: String, userData: UserData): Boolean {
        return set(uid, userData, true)
    }

    override suspend fun uploadAvatarWithCurrentUser(avatar: ByteArray): String? {
        val uid = sessionRepository.getUid().firstOrNull() ?: return null
        return uploadAvatar(uid, avatar)
    }

    override suspend fun uploadAvatar(uid: String, avatar: ByteArray): String? {
        val isLocal = sessionRepository.isLocal().firstOrNull() ?: return null

        val location = if (isLocal) {
            val filename = context.filesDir.resolve(File(SessionRepository.LOCAL_AVATAR_FILENAME))
            val file = FileOutputStream(filename)
            file.write(avatar)
            file.close()
            filename.toString()
        } else {
            val bucket = storage.from("avatars")
            val filename = "$uid.jpg"
            bucket.update(filename, avatar) { upsert = true }
            filename
        }

        return location
    }

    // ================================
    //  Remote operations
    // ================================

    override suspend fun remoteQuery(relation: String): List<UserDataDto>? {
        try {
            return database.from(TABLE_NAME).select {
                filter { UserDataDto::uuid eq relation }
            }.decodeList<UserDataDto>()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return null
        }
    }

    override suspend fun remoteUpsert(uuid: String): Boolean {
        return try {
            val entity = userDatabase.userDataDao.getByUuid(uuid).firstOrNull() ?: return false
            val dto = UserDataDto.fromEntity(entity)

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
                filter { UserDataDto::uuid eq uuid }
            }
            true
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            false
        }
    }

    // ================================
    //  Private Methods
    // ================================

    private fun queueWorker(uid: String) {
        val workRequest = OneTimeWorkRequestBuilder<UserSyncWorker>().setInputData(
            workDataOf(
                UserSyncWorker.KEY_TYPE to UserData::class.simpleName,
                UserSyncWorker.KEY_RELATION to uid
            )
        ).setConstraints(Constraints(NetworkType.CONNECTED)).build()

        workManager.enqueueUniqueWork(
            UserData::class.simpleName + UserSyncWorker.WORK_BASE_NAME,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
}
