package dev.bittim.valolink.feature.content.data.local.game.entity.agent

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.data.local.game.entity.GameEntity
import dev.bittim.valolink.feature.content.domain.model.agent.Recruitment

@Entity(
    tableName = "AgentRecruitments", foreignKeys = [ForeignKey(
        entity = AgentEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["agentUuid"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
data class RecruitmentEntity(
    @PrimaryKey val uuid: String,
    val agentUuid: String,
    override val version: String,
    val counterId: String,
    val milestoneId: String,
    val milestoneThreshold: Int,
    val useLevelVpCostOverride: Boolean,
    val levelVpCostOverride: Int,
    val startDate: String,
    val endDate: String
) : GameEntity() {
    fun toType(): Recruitment {
        return Recruitment(
            counterId,
            milestoneId,
            milestoneThreshold,
            useLevelVpCostOverride,
            levelVpCostOverride,
            startDate,
            endDate
        )
    }
}