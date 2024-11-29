package dev.bittim.valolink.core.data.repository.content.contract

import dev.bittim.valolink.core.data.repository.content.ContentRepository
import dev.bittim.valolink.main.domain.model.game.contract.Contract
import dev.bittim.valolink.main.domain.model.game.contract.chapter.Level
import dev.bittim.valolink.main.domain.model.game.contract.content.ContentType
import kotlinx.coroutines.flow.Flow

interface ContractRepository : ContentRepository<Contract> {
    suspend fun getByUuid(uuid: String, withRewards: Boolean): Flow<Contract?>
    suspend fun getAll(withRewards: Boolean): Flow<List<Contract>>

    suspend fun getActiveContracts(): Flow<List<Contract>>
    suspend fun getAgentGears(): Flow<List<Contract>>
    suspend fun getInactiveContracts(contentType: ContentType): Flow<List<Contract>>

    suspend fun getLevelByUuid(uuid: String): Flow<Level?>
    suspend fun getLevelByDependency(dependency: String): Flow<Level?>
}