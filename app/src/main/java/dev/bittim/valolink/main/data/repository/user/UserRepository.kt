package dev.bittim.valolink.main.data.repository.user

import dev.bittim.valolink.main.domain.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserRepository {
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