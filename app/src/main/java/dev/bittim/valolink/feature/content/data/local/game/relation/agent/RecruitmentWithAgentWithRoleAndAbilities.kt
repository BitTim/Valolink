package dev.bittim.valolink.feature.content.data.local.game.relation.agent

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.feature.content.data.local.game.entity.agent.AgentEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.agent.RecruitmentEntity
import dev.bittim.valolink.feature.content.domain.model.contract.Contract

data class RecruitmentWithAgentWithRoleAndAbilities(
    @Embedded val recruitment: RecruitmentEntity, @Relation(
        entity = AgentEntity::class, parentColumn = "uuid", entityColumn = "recruitment"
    ) val agentEntity: AgentWithRoleAndAbilities
) {
    fun toContract(): Contract {
        return recruitment.toContract(agentEntity.toType())
    }
}
