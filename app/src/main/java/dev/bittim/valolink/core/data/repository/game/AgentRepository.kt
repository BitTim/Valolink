package dev.bittim.valolink.core.data.repository.game

import dev.bittim.valolink.main.domain.model.game.agent.Agent
import kotlinx.coroutines.flow.Flow

interface AgentRepository : GameRepository<Agent> {
    suspend fun getAllBaseAgentUuids(): Flow<Set<String>>
}