package dev.bittim.valolink.main.data.repository.game

import dev.bittim.valolink.main.domain.model.game.contract.Contract
import dev.bittim.valolink.main.domain.model.game.contract.content.ContentType
import kotlinx.coroutines.flow.Flow

interface ContractRepository {
    suspend fun getContract(
        uuid: String,
        providedVersion: String? = null,
    ): Flow<Contract?>

    suspend fun getRecruitmentAsContract(
        uuid: String,
        providedVersion: String? = null,
    ): Flow<Contract?>

    suspend fun getActiveContracts(providedVersion: String? = null): Flow<List<Contract>>
    suspend fun getAgentGears(providedVersion: String? = null): Flow<List<Contract>>
    suspend fun getInactiveContracts(
        contentType: ContentType,
        providedVersion: String? = null,
    ): Flow<List<Contract>>

    suspend fun getAllContracts(providedVersion: String? = null): Flow<List<Contract>>

    suspend fun fetchContract(
        uuid: String,
        version: String,
    )
    suspend fun fetchAllContracts(version: String)

    fun queueWorker(version: String, uuid: String? = null)
}