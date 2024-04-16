package dev.bittim.valolink.feature.content.data.repository.game

import dev.bittim.valolink.feature.content.data.remote.game.dto.VersionDto
import dev.bittim.valolink.feature.content.domain.model.Event
import dev.bittim.valolink.feature.content.domain.model.Season
import dev.bittim.valolink.feature.content.domain.model.agent.Agent
import dev.bittim.valolink.feature.content.domain.model.contract.Contract
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    suspend fun getApiVersion(): VersionDto?
    suspend fun getSeason(uuid: String): Flow<Season>
    suspend fun getAllSeasons(): Flow<List<Season>>
    suspend fun getEvent(uuid: String): Flow<Event>
    suspend fun getAllEvents(): Flow<List<Event>>
    
    suspend fun getAllContracts(): Flow<List<Contract>>

    suspend fun getAgent(uuid: String): Flow<Agent>
    suspend fun getAllAgents(): Flow<List<Agent>>
}