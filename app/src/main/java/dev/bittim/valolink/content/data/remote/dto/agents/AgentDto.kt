package dev.bittim.valolink.content.data.remote.dto.agents

import dev.bittim.valolink.content.data.local.entity.agent.AgentEntity

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
	val voiceLine: Any?,
) {
	fun toEntity(version: String): AgentEntity {
		return AgentEntity(
			uuid,
			version,
			displayName,
			description,
			developerName,
			characterTags ?: emptyList(),
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
			isBaseContent,
			role.uuid,
			recruitmentData?.milestoneId
		)
	}
}