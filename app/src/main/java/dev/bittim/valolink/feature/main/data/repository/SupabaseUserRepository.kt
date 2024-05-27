package dev.bittim.valolink.feature.main.data.repository

import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import javax.inject.Inject

class SupabaseUserRepository @Inject constructor(
    private val auth: Auth,
    private val database: Postgrest,
    private val realtime: Realtime,
) : UserRepository {
    override fun getSessionStatus(): StateFlow<SessionStatus> {
        return auth.sessionStatus
    }

    override suspend fun getUser(): UserInfo? {
        return auth.currentUserOrNull()
    }

    override suspend fun getUid(): String? {
        return getUser()?.id
    }

    override suspend fun getHasOnboarded(): Boolean? {
        return getUser()?.userMetadata?.get("hasOnboarded")?.jsonPrimitive?.booleanOrNull
    }

    override suspend fun signOut() {
        auth.signOut()
    }



    override suspend fun setOnboardingComplete(ownedAgentUuids: List<String>): Boolean {
        auth.updateUser {
            data {
                put(
                    "hasOnboarded",
                    true
                )
            }
        }

        return true

        //        return try {
        //            databases.createDocument(
        //                "main",
        //                "users",
        //                uid,
        //                mapOf(
        //                    "agents" to ownedAgentUuids
        //                ),
        //                listOf(
        //                    Permission.read(Role.user(uid)),
        //                    Permission.write(Role.user(uid))
        //                )
        //            )
        //            true
        //        } catch (e: AppwriteException) {
        //            e.printStackTrace()
        //            false
        //        }
    }

    //    override suspend fun getUserData(): Flow<UserData?> {
    //            val uid = getUid() ?: return flow { }
    //                return callbackFlow {
    //                    var subscription: RealtimeSubscription? = null
    //
    //                    try {
    //                        val data = databases.getDocument(
    //                            "main",
    //                            "users",
    //                            uid
    //                        ).data
    //
    //                        trySend(UserData.from(data))
    //
    //                        subscription = realtime.subscribe(
    //                            "databases.main.collections.users.documents.$uid"
    //                        ) {
    //                            trySend(UserData.from(it.payload.jsonCast()))
    //                        }
    //                    } catch (e: AppwriteException) {
    //                        e.printStackTrace()
    //                    }
    //
    //                    awaitClose { subscription?.close() }
    //                }
    //    }

    //    override suspend fun setUserData(userData: UserData): Boolean {
    //            val uid = getUid() ?: return false
    //                return try {
    //                    databases.updateDocument(
    //                        "main",
    //                        "users",
    //                        uid,
    //                        userData
    //                    )
    //                    true
    //                } catch (e: AppwriteException) {
    //                    e.printStackTrace()
    //                    false
    //                }
    //    }
}