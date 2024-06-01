package dev.bittim.valolink.main.data.local.game.relation.agent

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.main.data.local.game.entity.agent.AgentEntity
import dev.bittim.valolink.main.data.local.game.entity.agent.RecruitmentEntity
import dev.bittim.valolink.main.domain.model.game.contract.Contract

data class RecruitmentWithAgentWithRoleAndAbilities(
    @Embedded val recruitment: RecruitmentEntity,
    @Relation(
        entity = AgentEntity::class,
        parentColumn = "uuid",
        entityColumn = "recruitment"
    ) val agentEntity: AgentWithRoleAndAbilities,
) {
    fun toContract(): Contract {
        return recruitment.toContract(agentEntity.toType())
    }
}
