package dev.bittim.valolink.core.data.local.content.relation.agent

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.core.data.local.content.entity.VersionedEntity
import dev.bittim.valolink.core.data.local.content.entity.agent.AbilityEntity
import dev.bittim.valolink.core.data.local.content.entity.agent.AgentEntity
import dev.bittim.valolink.core.data.local.content.entity.agent.RoleEntity
import dev.bittim.valolink.main.domain.model.game.agent.Agent

data class AgentWithRoleAndAbilities(
	@Embedded val agent: AgentEntity,
	@Relation(
		parentColumn = "role", entityColumn = "uuid"
	) val role: RoleEntity,
	@Relation(
		parentColumn = "uuid", entityColumn = "agentUuid"
	) val abilities: Set<AbilityEntity>,
) : VersionedEntity {
	override fun getApiVersion(): String {
		return agent.version
	}

	fun toType(): Agent {
		return agent.toType(role.toType(),
							abilities.map { it.toType() }.filter { it.displayIcon != null }
								.sortedBy { it.slot })
	}
}