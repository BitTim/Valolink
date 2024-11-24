package dev.bittim.valolink.main.data.repository.user.data

import dev.bittim.valolink.main.data.remote.user.dto.UserLevelDto
import dev.bittim.valolink.main.domain.model.user.UserLevel
import kotlinx.coroutines.flow.Flow

interface UserLevelRepository : UserRepository<UserLevel, UserLevelDto> {
	suspend fun getAll(uid: String): Flow<List<UserLevel>>
	suspend fun get(uid: String, uuid: String): Flow<UserLevel?>

	suspend fun set(obj: UserLevel, toDelete: Boolean): Boolean
	suspend fun set(obj: UserLevel): Boolean
	suspend fun delete(obj: UserLevel): Boolean

	fun queueWorker(userContract: String)
}