package dev.bittim.valolink.feature.main.data.repository.game

import dev.bittim.valolink.feature.main.domain.model.game.agent.Agent
import kotlinx.coroutines.flow.Flow

interface AgentRepository {
    suspend fun getAgent(uuid: String, providedVersion: String? = null): Flow<Agent>
    suspend fun getAllAgents(providedVersion: String? = null): Flow<List<Agent>>
    suspend fun getAllBaseAgentUuids(providedVersion: String? = null): Flow<List<String>>

    suspend fun fetchAgent(uuid: String, version: String)
    suspend fun fetchAgents(version: String)
}