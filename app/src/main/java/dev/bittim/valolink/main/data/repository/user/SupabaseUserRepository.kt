package dev.bittim.valolink.main.data.repository.user

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.main.data.local.user.UserDatabase
import dev.bittim.valolink.main.data.local.user.entity.UserDataEntity
import dev.bittim.valolink.main.data.worker.user.UserSyncWorker
import dev.bittim.valolink.main.domain.model.UserData
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import javax.inject.Inject

class SupabaseUserRepository @Inject constructor(
    private val auth: Auth,
    private val userDatabase: UserDatabase,
    private val workManager: WorkManager,
) : UserRepository, DefaultLifecycleObserver {
    // ================================
    //  Session
    // ================================

    override fun getSessionStatus(): StateFlow<SessionStatus> {
        return auth.sessionStatus
    }

    override suspend fun signOut() {
        userDatabase.userDataDao.deleteAll()
        auth.signOut()
    }

    // ================================
    //  User Metadata
    // ================================

    override suspend fun getUser(): UserInfo? {
        return auth.currentUserOrNull()
    }

    override suspend fun getUid(): String? {
        return getUser()?.id
    }

    override suspend fun getHasOnboarded(): Boolean? {
        return getUser()?.userMetadata?.get("hasOnboarded")?.jsonPrimitive?.booleanOrNull
    }

    override suspend fun getUsernameFromMetadata(): String? {
        return getUser()?.userMetadata?.get("display_name")?.jsonPrimitive?.contentOrNull
    }

    // ================================
    //  Onboarding
    // ================================

    override suspend fun setOnboardingComplete(
        ownedAgentUuids: List<String>,
    ): Boolean {
        auth.updateUser {
            data {
                put(
                    "hasOnboarded",
                    true
                )
            }
        }

        val uid = getUid() ?: return false
        val username = getUsernameFromMetadata() ?: return false

        return setCurrentUserData(
            UserData(
                uid,
                isPrivate = false,
                username,
                ownedAgentUuids
            )
        )
    }

    // ================================
    //  Get User Data
    // ================================

    override suspend fun getCurrentUserData(): Flow<UserData?> {
        val uid = getUid() ?: return flow { }
        return getUserData(uid)
    }

    override suspend fun getUserData(uid: String): Flow<UserData?> {
        return try {
            // Get from local database
            val localUserFlow =
                userDatabase.userDataDao.getByUuid(uid).distinctUntilChanged().map { it?.toType() }

            // Queue worker to sync with Supabase
            val workRequest = OneTimeWorkRequestBuilder<UserSyncWorker>()
                .setInputData(workDataOf(UserSyncWorker.KEY_UID to uid))
                .setConstraints(Constraints(NetworkType.CONNECTED))
                .build()
            workManager.enqueueUniqueWork(
                "UserSyncWorker",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )

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
        val uid = getUid() ?: return false
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
            val workRequest = OneTimeWorkRequestBuilder<UserSyncWorker>()
                .setInputData(workDataOf(UserSyncWorker.KEY_UID to uid))
                .setConstraints(Constraints(NetworkType.CONNECTED))
                .build()
            workManager.enqueueUniqueWork(
                "UserSyncWorker",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )

            // Return
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}