package dev.bittim.valolink.main.data.repository.game

import dev.bittim.valolink.main.domain.model.game.contract.Contract
import dev.bittim.valolink.main.domain.model.game.contract.chapter.ChapterLevel
import dev.bittim.valolink.main.domain.model.game.contract.content.ContentType
import kotlinx.coroutines.flow.Flow

interface ContractRepository : GameRepository<Contract> {
    suspend fun getByUuid(uuid: String, withRewards: Boolean): Flow<Contract?>
    suspend fun getAll(withRewards: Boolean): Flow<List<Contract>>

    suspend fun getRecruitmentAsContract(uuid: String): Flow<Contract?>

    suspend fun getActiveContracts(): Flow<List<Contract>>
    suspend fun getAgentGears(): Flow<List<Contract>>
    suspend fun getInactiveContracts(contentType: ContentType): Flow<List<Contract>>

    suspend fun getLevelByUuid(uuid: String): Flow<ChapterLevel?>
}