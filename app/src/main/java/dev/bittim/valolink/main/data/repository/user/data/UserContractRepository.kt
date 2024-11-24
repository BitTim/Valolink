package dev.bittim.valolink.main.data.repository.user.data

import dev.bittim.valolink.main.data.remote.user.dto.UserContractDto
import dev.bittim.valolink.main.domain.model.user.UserContract
import kotlinx.coroutines.flow.Flow

interface UserContractRepository : UserRepository<UserContract, UserContractDto> {
	suspend fun getAllWithCurrentUser(): Flow<List<UserContract>>
	suspend fun getAll(uid: String): Flow<List<UserContract>>
	suspend fun getWithCurrentUser(uuid: String): Flow<UserContract?>
	suspend fun get(uid: String, uuid: String): Flow<UserContract?>

	suspend fun set(obj: UserContract, toDelete: Boolean): Boolean
	suspend fun set(obj: UserContract): Boolean
	suspend fun delete(obj: UserContract?): Boolean

	fun queueWorker(uid: String)
}