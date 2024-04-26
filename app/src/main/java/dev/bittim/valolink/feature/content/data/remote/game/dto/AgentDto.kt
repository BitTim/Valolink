package dev.bittim.valolink.feature.content.data.remote.game.dto

import dev.bittim.valolink.feature.content.data.local.game.entity.agent.AbilityEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.agent.AgentEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.agent.RecruitmentEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.agent.RoleEntity

data class AgentDto(
    val uuid: String,
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
    val role: RoleDto,
    val recruitmentData: RecruitmentDto?,
    val abilities: List<AbilityDto>,
    val voiceLine: Any?
) {
    fun toEntity(version: String): AgentEntity {
        return AgentEntity(
            uuid,
            version,
            displayName,
            description,
            developerName,
            characterTags ?: listOf(),
            displayIcon,
            displayIconSmall,
            bustPortrait,
            fullPortrait,
            fullPortraitV2,
            killfeedPortrait,
            background,
            backgroundGradientColors,
            assetPath,
            isFullPortraitRightFacing,
            isPlayableCharacter,
            isAvailableForTest,
            isBaseContent, role.uuid, recruitmentData?.milestoneId
        )
    }



    data class RecruitmentDto(
        val counterId: String,
        val milestoneId: String,
        val milestoneThreshold: Int,
        val useLevelVpCostOverride: Boolean,
        val levelVpCostOverride: Int,
        val startDate: String,
        val endDate: String
    ) {
        fun toEntity(version: String): RecruitmentEntity {
            return RecruitmentEntity(
                milestoneId,
                version,
                milestoneThreshold,
                useLevelVpCostOverride,
                levelVpCostOverride,
                startDate,
                endDate
            )
        }
    }

    data class RoleDto(
        val uuid: String,
        val displayName: String,
        val description: String,
        val displayIcon: String,
        val assetPath: String
    ) {
        fun toEntity(version: String): RoleEntity {
            return RoleEntity(
                uuid, version, displayName, description, displayIcon, assetPath
            )
        }
    }

    data class AbilityDto(
        val slot: String, val displayName: String, val description: String, val displayIcon: String?
    ) {
        fun toEntity(version: String, agentUuid: String): AbilityEntity {
            return AbilityEntity(
                agentUuid + slot,
                agentUuid,
                version,
                slot,
                displayName,
                description,
                displayIcon
            )
        }
    }
}