package dev.bittim.valolink.core.data.local.content.relation.agent

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.core.data.local.content.entity.VersionedEntity
import dev.bittim.valolink.core.data.local.content.entity.agent.AgentEntity
import dev.bittim.valolink.core.data.local.content.entity.agent.RecruitmentEntity
import dev.bittim.valolink.main.domain.model.game.contract.Contract

data class RecruitmentWithAgentWithRoleAndAbilities(
	@Embedded val recruitment: RecruitmentEntity,
	@Relation(
		entity = AgentEntity::class, parentColumn = "uuid", entityColumn = "recruitment"
	) val agentEntity: AgentWithRoleAndAbilities,
) : VersionedEntity {
	override fun getApiVersion(): String {
		return recruitment.version
	}

	fun toContract(): Contract {
		return recruitment.toContract(agentEntity.toType())
	}
}
