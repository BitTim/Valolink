package dev.bittim.valolink.content.data.local.relation.agent

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.data.local.entity.agent.AgentEntity
import dev.bittim.valolink.content.data.local.entity.agent.RecruitmentEntity
import dev.bittim.valolink.content.domain.model.contract.Contract

data class RecruitmentWithAgentWithRoleAndAbilities(
    @Embedded val recruitment: RecruitmentEntity,
    @Relation(
        entity = AgentEntity::class, parentColumn = "uuid", entityColumn = "recruitment"
    ) val agentEntity: AgentWithRoleAndAbilities,
) : VersionedEntity {
    override val version: String
        get() = recruitment.version

    fun toContract(): Contract {
        return recruitment.toContract(agentEntity.toType())
    }
}
