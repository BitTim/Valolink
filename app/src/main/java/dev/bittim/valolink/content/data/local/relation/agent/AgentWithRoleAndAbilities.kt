/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       AgentWithRoleAndAbilities.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.local.relation.agent

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.data.local.entity.agent.AbilityEntity
import dev.bittim.valolink.content.data.local.entity.agent.AgentEntity
import dev.bittim.valolink.content.data.local.entity.agent.RoleEntity
import dev.bittim.valolink.content.domain.model.agent.Agent

data class AgentWithRoleAndAbilities(
    @Embedded val agent: AgentEntity,
    @Relation(
        parentColumn = "role", entityColumn = "uuid"
    ) val role: RoleEntity,
    @Relation(
        parentColumn = "uuid", entityColumn = "agentUuid"
    ) val abilities: Set<AbilityEntity>,
) : VersionedEntity {
    override val uuid: String
        get() = agent.uuid
    override val version: String
        get() = agent.version

    fun toType(): Agent {
        return agent.toType(role.toType(),
            abilities.map { it.toType() }.filter { it.displayIcon != null }
                .sortedBy { it.slot })
    }
}