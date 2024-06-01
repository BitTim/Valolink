package dev.bittim.valolink.main.data.repository.user

import dev.bittim.valolink.main.domain.model.UserData
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {

    // ================================
    //  Session
    // ================================

    fun getSessionStatus(): StateFlow<SessionStatus>
    suspend fun signOut()

    // ================================
    //  User Metadata
    // ================================

    suspend fun getUser(): UserInfo?
    suspend fun getUid(): String?
    suspend fun getHasOnboarded(): Boolean?
    suspend fun getUsernameFromMetadata(): String?

    // ================================
    //  Onboarding
    // ================================

    suspend fun setOnboardingComplete(
        ownedAgentUuids: List<String>,
    ): Boolean

    // ================================
    //  Get User Data
    // ================================

    suspend fun getCurrentUserData(): Flow<UserData?>
    suspend fun getUserData(uid: String): Flow<UserData?>

    // ================================
    //  Set User Data
    // ================================

    suspend fun setCurrentUserData(userData: UserData): Boolean
    suspend fun setUserData(
        uid: String,
        userData: UserData,
    ): Boolean
}
