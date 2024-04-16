package dev.bittim.valolink.feature.content.data.local.game.relation.agent

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.feature.content.data.local.game.entity.agent.AbilityEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.agent.AgentEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.agent.RecruitmentEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.agent.RoleEntity
import dev.bittim.valolink.feature.content.domain.model.agent.Agent

data class AgentWithRecruitmentAndRoleAndAbilities(
    @Embedded val agent: AgentEntity, @Relation(
        parentColumn = "uuid", entityColumn = "agentUuid"
    ) val recruitment: RecruitmentEntity?, @Relation(
        parentColumn = "role", entityColumn = "uuid"
    ) val role: RoleEntity, @Relation(
        parentColumn = "uuid", entityColumn = "agentUuid"
    ) val abilities: List<AbilityEntity>
) {
    fun toType(): Agent {
        return agent.toType(role.toType(), recruitment?.toType(), abilities.map { it.toType() })
    }
}