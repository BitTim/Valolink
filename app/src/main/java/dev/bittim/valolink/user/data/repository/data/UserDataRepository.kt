package dev.bittim.valolink.user.data.repository.data

import dev.bittim.valolink.user.data.remote.dto.UserDataDto
import dev.bittim.valolink.user.domain.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository : UserRepository<UserData, UserDataDto> {
    // ================================
    //  Get User Data
    // ================================

    suspend fun getWithCurrentUser(): Flow<UserData?>
    suspend fun get(uid: String): Flow<UserData?>

    // ================================
    //  Set User Data
    // ================================

    suspend fun setWithCurrentUser(userData: UserData): Boolean

    suspend fun set(uid: String, userData: UserData, toDelete: Boolean): Boolean
    suspend fun set(uid: String, userData: UserData): Boolean
    suspend fun delete(uid: String, userData: UserData): Boolean
}