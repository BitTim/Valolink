package dev.bittim.valolink.main.data.repository.user

import androidx.lifecycle.DefaultLifecycleObserver
import dev.bittim.valolink.main.data.local.user.UserDatabase
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserUpdateBuilder
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

class SessionSupabaseRepository @Inject constructor(
    private val auth: Auth,
    private val userDatabase: UserDatabase,
) : SessionRepository, DefaultLifecycleObserver {
    // ================================
    //  Session
    // ================================

    override fun getSessionStatus(): StateFlow<SessionStatus> {
        return auth.sessionStatus
    }

    override suspend fun signOut() {
        // Delete all cached user data on sign out
        userDatabase.userDataDao.deleteAll()
        userDatabase.userContractDao.deleteAll()

        // Sign out
        auth.signOut()
    }

    // ================================
    //  User Metadata
    // ================================

    override suspend fun getUserInfo(): UserInfo? {
        return auth.currentUserOrNull()
    }

    override suspend fun getUid(): String? {
        return getUserInfo()?.id
    }

    override suspend fun getHasOnboarded(): Boolean? {
        return getUserInfo()?.userMetadata?.get("hasOnboarded")?.jsonPrimitive?.booleanOrNull
    }

    override suspend fun getUsernameFromMetadata(): String? {
        return getUserInfo()?.userMetadata?.get("display_name")?.jsonPrimitive?.contentOrNull
    }

    override suspend fun updateUserInfo(userInfo: UserUpdateBuilder.() -> Unit) {
        auth.updateUser(config = userInfo)
    }
}