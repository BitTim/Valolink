package dev.bittim.valolink.content.data.repository.agent

import dev.bittim.valolink.content.data.repository.ContentRepository
import dev.bittim.valolink.content.domain.model.agent.Agent
import kotlinx.coroutines.flow.Flow

interface AgentRepository : ContentRepository<Agent> {
    suspend fun getAllBaseAgentUuids(): Flow<Set<String>>
}