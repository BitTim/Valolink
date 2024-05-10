package dev.bittim.valolink.feature.content.data.repository.game

import dev.bittim.valolink.feature.content.domain.model.agent.Agent
import kotlinx.coroutines.flow.Flow

interface AgentRepository {
    suspend fun getAgent(uuid: String, providedVersion: String? = null): Flow<Agent>
    suspend fun getAllAgents(providedVersion: String? = null): Flow<List<Agent>>

    suspend fun fetchAgent(uuid: String, version: String)
    suspend fun fetchAgents(version: String)
}