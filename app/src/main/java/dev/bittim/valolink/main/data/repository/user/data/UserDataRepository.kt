package dev.bittim.valolink.main.data.repository.user.data

import dev.bittim.valolink.main.data.remote.user.dto.UserDataDto
import dev.bittim.valolink.main.domain.model.user.UserData
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