package dev.bittim.valolink.main.data.repository.user.data

import dev.bittim.valolink.main.data.remote.user.dto.UserAgentDto
import dev.bittim.valolink.main.domain.model.user.UserAgent
import kotlinx.coroutines.flow.Flow

interface UserAgentRepository : UserRepository<UserAgent, UserAgentDto> {
	suspend fun getAllWithCurrentUser(): Flow<List<UserAgent>>
	suspend fun getAll(uid: String): Flow<List<UserAgent>>
	suspend fun getWithCurrentUser(uuid: String): Flow<UserAgent?>
	suspend fun get(uid: String, uuid: String): Flow<UserAgent?>

	suspend fun set(obj: UserAgent, toDelete: Boolean): Boolean
	suspend fun set(obj: UserAgent): Boolean
	suspend fun delete(obj: UserAgent): Boolean

	fun queueWorker(uid: String)
}