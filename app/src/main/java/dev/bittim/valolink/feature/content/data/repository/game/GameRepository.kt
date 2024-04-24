package dev.bittim.valolink.feature.content.data.repository.game

import dev.bittim.valolink.feature.content.data.remote.game.dto.VersionDto
import dev.bittim.valolink.feature.content.domain.model.Event
import dev.bittim.valolink.feature.content.domain.model.Season
import dev.bittim.valolink.feature.content.domain.model.agent.Agent
import dev.bittim.valolink.feature.content.domain.model.contract.Contract
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    suspend fun getApiVersion(): VersionDto?
    suspend fun getSeason(uuid: String, providedVersion: String? = null): Flow<Season>
    suspend fun getAllSeasons(providedVersion: String? = null): Flow<List<Season>>
    suspend fun getEvent(uuid: String, providedVersion: String? = null): Flow<Event>
    suspend fun getAllEvents(providedVersion: String? = null): Flow<List<Event>>

    suspend fun getContract(uuid: String, providedVersion: String? = null): Flow<Contract>
    suspend fun getAllContracts(providedVersion: String? = null): Flow<List<Contract>>
    suspend fun getAllRecruitmentsAsContracts(providedVersion: String? = null): Flow<List<Contract>>
    suspend fun getAllContractsAndRecruitments(providedVersion: String? = null): Flow<List<Contract>>

    suspend fun getAllActiveContracts(providedVersion: String? = null): Flow<List<Contract>>
    suspend fun getAllAgentGears(providedVersion: String? = null): Flow<List<Contract>>
    suspend fun getAllInactiveContracts(providedVersion: String? = null): Flow<List<Contract>>

    suspend fun getAgent(uuid: String, providedVersion: String? = null): Flow<Agent>
    suspend fun getAllAgents(providedVersion: String? = null): Flow<List<Agent>>
}