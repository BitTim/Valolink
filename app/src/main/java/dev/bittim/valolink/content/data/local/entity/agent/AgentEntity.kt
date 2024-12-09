package dev.bittim.valolink.content.data.local.entity.agent

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.domain.model.agent.Ability
import dev.bittim.valolink.content.domain.model.agent.Agent
import dev.bittim.valolink.content.domain.model.agent.Role

@Entity(
    tableName = "Agents", foreignKeys = [ForeignKey(
        entity = RoleEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["role"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = RecruitmentEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["recruitment"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(
        value = ["uuid"], unique = true
    ), Index(
        value = ["role"], unique = false
    ), Index(
        value = ["recruitment"], unique = true
    )]
)
data class AgentEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val displayName: String,
    val description: String,
    val developerName: String,
    val characterTags: List<String>,
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
    val recruitment: String?,
) : VersionedEntity {
    fun toType(
        role: Role,
        abilities: List<Ability>,
    ): Agent {
        return Agent(
            uuid,
            displayName,
            description,
            developerName,
            characterTags,
            displayIcon,
            displayIconSmall,
            bustPortrait,
            fullPortrait,
            fullPortraitV2,
            killfeedPortrait,
            background,
            backgroundGradientColors,
            isFullPortraitRightFacing,
            isAvailableForTest,
            isBaseContent,
            role,
            abilities
        )
    }
}