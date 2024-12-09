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
    override val version: String
        get() = agent.version

    fun toType(): Agent {
        return agent.toType(role.toType(),
            abilities.map { it.toType() }.filter { it.displayIcon != null }
                .sortedBy { it.slot })
    }
}