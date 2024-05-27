package dev.bittim.valolink.feature.main.data.repository

import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    fun getSessionStatus(): StateFlow<SessionStatus>
    suspend fun getUser(): UserInfo?
    suspend fun signOut()

    suspend fun getUid(): String?

    suspend fun setOnboardingComplete(ownedAgentUuids: List<String>): Boolean
    suspend fun getHasOnboarded(): Boolean?

    //    suspend fun getUserData(): Flow<UserData?>
    //    suspend fun setUserData(userData: UserData): Boolean
}