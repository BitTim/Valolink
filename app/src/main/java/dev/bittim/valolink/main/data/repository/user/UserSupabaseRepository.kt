package dev.bittim.valolink.main.data.repository.user

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.main.data.local.user.UserDatabase
import dev.bittim.valolink.main.data.local.user.entity.UserDataEntity
import dev.bittim.valolink.main.data.worker.user.UserDataSyncWorker
import dev.bittim.valolink.main.domain.model.user.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserSupabaseRepository @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userDatabase: UserDatabase,
    private val workManager: WorkManager,
) : UserRepository {
    // ================================
    //  Get User Data
    // ================================

    override suspend fun getCurrentUserData(): Flow<UserData?> {
        val uid = sessionRepository.getUid() ?: return flow { }
        return getUserData(uid)
    }

    override suspend fun getUserData(uid: String): Flow<UserData?> {
        return try {
            // Get from local database
            val localUserFlow =
                userDatabase.userDataDao.getByUuid(uid).distinctUntilChanged().map { it?.toType() }

            // Queue worker to sync with Supabase
            queueWorker(uid)

            // Return
            localUserFlow
        } catch (e: Exception) {
            e.printStackTrace()
            return flow { }
        }
    }

    // ================================
    //  Set User Data
    // ================================

    override suspend fun setCurrentUserData(userData: UserData): Boolean {
        val uid = sessionRepository.getUid() ?: return false
        return setUserData(
            uid,
            userData
        )
    }

    override suspend fun setUserData(
        uid: String,
        userData: UserData,
    ): Boolean {
        return try {
            // Insert into local database
            userDatabase.userDataDao.upsert(UserDataEntity.fromType(userData))

            // Queue worker to sync with Supabase
            queueWorker(uid)

            // Return
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // ================================
    //  Private Methods
    // ================================

    private fun queueWorker(uid: String) {
        val workRequest = OneTimeWorkRequestBuilder<UserDataSyncWorker>()
            .setInputData(workDataOf(UserDataSyncWorker.KEY_UID to uid))
            .setConstraints(Constraints(NetworkType.CONNECTED))
            .build()
        workManager.enqueueUniqueWork(
            "UserDataSyncWorker",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
}