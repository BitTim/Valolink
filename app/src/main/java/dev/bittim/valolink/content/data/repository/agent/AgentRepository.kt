/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       AgentRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.repository.agent

import dev.bittim.valolink.content.data.repository.ContentRepository
import dev.bittim.valolink.content.domain.model.agent.Agent
import kotlinx.coroutines.flow.Flow

interface AgentRepository : ContentRepository<Agent> {
    suspend fun getAllBaseAgentUuids(): Flow<Set<String>>
}