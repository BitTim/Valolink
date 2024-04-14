package dev.bittim.valolink.feature.content.data.local.game.entity.agent

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.data.local.game.entity.GameEntity
import dev.bittim.valolink.feature.content.domain.model.agent.Agent

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = RoleEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["role"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AgentEntity(
    @PrimaryKey
    val uuid: String,
    override val version: String,
    val displayName: String,
    val description: String,
    val developerName: String,
    val characterTags: List<String>?,
    val displayIcon: String,
    val displayIconSmall: String,
    val bustPortrait: String,
    val fullPortrait: String,
    val fullPortraitV2: String,
    val killfeedPortrait: String,
    val background: String,
    val backgroundGradientColors: List<String>,
    val assetPath: String,
    val isFullPortraitRightFacing: Boolean,
    val isPlayableCharacter: Boolean,
    val isAvailableForTest: Boolean,
    val isBaseContent: Boolean,
    val role: String,
    @Embedded val recruitmentData: RecruitmentData?,
    @Embedded val voiceLine: VoiceLine?
) : GameEntity<Agent>() {
    data class RecruitmentData(
        val counterId: String,
        val milestoneId: String,
        val milestoneThreshold: Int,
        val useLevelVpCostOverride: Boolean,
        val levelVpCostOverride: Int,
        val startDate: String,
        val endDate: String
    )

    data class VoiceLine(
        val minDuration: Float,
        val maxDuration: Float,
        val mediaList: List<Media>
    ) {
        data class Media(
            val id: Int,
            val wwise: String,
            val wave: String
        )
    }


    fun toType(): Agent {
        return Agent(
            uuid = uuid
        )
    }
}