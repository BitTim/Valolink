package dev.bittim.valolink.feature.main.data.repository

import dev.bittim.valolink.feature.main.domain.model.UserData
import io.appwrite.Permission
import io.appwrite.Role
import io.appwrite.exceptions.AppwriteException
import io.appwrite.extensions.jsonCast
import io.appwrite.models.Preferences
import io.appwrite.models.RealtimeSubscription
import io.appwrite.models.User
import io.appwrite.services.Account
import io.appwrite.services.Databases
import io.appwrite.services.Realtime
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AppwriteUserRepository @Inject constructor(
    private val realtime: Realtime,
    private val account: Account,
    private val databases: Databases,
) : UserRepository {
    override suspend fun getUser(): User<Map<String, Any>>? {
        return try {
            account.get()
        } catch (e: AppwriteException) {
            null
        }
    }

    override suspend fun getUserPrefs(): Preferences<Map<String, Any>>? {
        return getUser()?.prefs
    }

    override suspend fun hasUser(): Boolean {
        return getUser() != null
    }

    override fun hasUserAsFlow(interval: Long): Flow<Boolean> = flow {
        while (true) {
            emit(hasUser())
            delay(interval)
        }
    }

    override suspend fun signOut() {
        try {
            account.deleteSession("current")
        } catch (e: AppwriteException) {
            e.printStackTrace()
        }
    }



    override suspend fun getUid(): String? {
        return getUser()?.id
    }

    override suspend fun getUsername(): String? {
        return getUser()?.name
    }



    override suspend fun setOnboardingComplete(ownedAgentUuids: List<String>): Boolean {
        val uid = getUid() ?: return false
        account.updatePrefs(
            mapOf(
                "hasOnboarded" to true
            )
        )

        return try {
            databases.createDocument(
                "main",
                "users",
                uid,
                mapOf(
                    "agents" to ownedAgentUuids
                ),
                listOf(
                    Permission.read(Role.user(uid)),
                    Permission.write(Role.user(uid))
                )
            )
            true
        } catch (e: AppwriteException) {
            e.printStackTrace()
            false
        }
    }



    override suspend fun getUserData(): Flow<UserData?> {
        val uid = getUid() ?: return flow { }
        return callbackFlow {
            var subscription: RealtimeSubscription? = null

            try {
                val data = databases.getDocument(
                    "main",
                    "users",
                    uid
                ).data

                trySend(UserData.from(data))

                subscription = realtime.subscribe(
                    "databases.main.collections.users.documents.$uid"
                ) {
                    trySend(UserData.from(it.payload.jsonCast()))
                }
            } catch (e: AppwriteException) {
                e.printStackTrace()
            }

            awaitClose { subscription?.close() }
        }
    }

    override suspend fun setUserData(userData: UserData): Boolean {
        val uid = getUid() ?: return false
        return try {
            databases.updateDocument(
                "main",
                "users",
                uid,
                userData
            )
            true
        } catch (e: AppwriteException) {
            e.printStackTrace()
            false
        }
    }
}