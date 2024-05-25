package dev.bittim.valolink.feature.main.data.repository.game

import dev.bittim.valolink.feature.main.domain.model.game.contract.Contract
import kotlinx.coroutines.flow.Flow

interface ContractRepository {
    suspend fun getContract(uuid: String, providedVersion: String? = null): Flow<Contract?>
    suspend fun getRecruitmentAsContract(
        uuid: String, providedVersion: String? = null
    ): Flow<Contract?>

    //suspend fun getContractWithRewards(uuid: String, providedVersion: String? = null): Flow<Contract?>
    //suspend fun getRecruitmentAsContractWithRewards(uuid: String, providedVersion: String? = null): Flow<Contract?>

    suspend fun getActiveContracts(providedVersion: String? = null): Flow<List<Contract>>
    suspend fun getAgentGears(providedVersion: String? = null): Flow<List<Contract>>
    suspend fun getInactiveContracts(
        contentType: String, providedVersion: String? = null
    ): Flow<List<Contract>>

    suspend fun fetchContract(uuid: String, version: String)
    suspend fun fetchContracts(version: String)
}