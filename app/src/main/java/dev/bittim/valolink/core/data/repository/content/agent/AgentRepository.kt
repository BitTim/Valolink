package dev.bittim.valolink.core.data.repository.content.agent

import dev.bittim.valolink.core.data.repository.content.ContentRepository
import dev.bittim.valolink.main.domain.model.game.agent.Agent
import kotlinx.coroutines.flow.Flow

interface AgentRepository : ContentRepository<Agent> {
    suspend fun getAllBaseAgentUuids(): Flow<Set<String>>
}