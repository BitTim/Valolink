package dev.bittim.valolink.core.data.repository.game

import dev.bittim.valolink.main.domain.model.game.contract.Contract
import dev.bittim.valolink.main.domain.model.game.contract.chapter.Level
import dev.bittim.valolink.main.domain.model.game.contract.content.ContentType
import kotlinx.coroutines.flow.Flow

interface ContractRepository : GameRepository<Contract> {
    suspend fun getByUuid(uuid: String, withRewards: Boolean): Flow<Contract?>
    suspend fun getAll(withRewards: Boolean): Flow<List<Contract>>

    suspend fun getActiveContracts(): Flow<List<Contract>>
    suspend fun getAgentGears(): Flow<List<Contract>>
    suspend fun getInactiveContracts(contentType: ContentType): Flow<List<Contract>>

    suspend fun getLevelByUuid(uuid: String): Flow<Level?>
    suspend fun getLevelByDependency(dependency: String): Flow<Level?>
}