package dev.bittim.valolink.feature.main.data.repository

import dev.bittim.valolink.feature.main.domain.model.UserData
import io.appwrite.models.Preferences
import io.appwrite.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUser(): User<Map<String, Any>>?
    suspend fun getUserPrefs(): Preferences<Map<String, Any>>?
    suspend fun hasUser(): Boolean
    fun hasUserAsFlow(interval: Long): Flow<Boolean>
    suspend fun signOut()

    suspend fun getUid(): String?
    suspend fun getUsername(): String?

    suspend fun setOnboardingComplete(ownedAgentUuids: List<String>): Boolean

    suspend fun getUserData(): Flow<UserData?>
    suspend fun setUserData(userData: UserData): Boolean
}