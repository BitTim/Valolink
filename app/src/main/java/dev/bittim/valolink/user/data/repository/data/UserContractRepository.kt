package dev.bittim.valolink.user.data.repository.data

import dev.bittim.valolink.user.data.remote.dto.UserContractDto
import dev.bittim.valolink.user.domain.model.UserContract
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