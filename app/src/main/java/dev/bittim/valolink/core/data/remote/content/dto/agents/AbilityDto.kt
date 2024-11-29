package dev.bittim.valolink.core.data.remote.content.dto.agents

import dev.bittim.valolink.core.data.local.content.entity.agent.AbilityEntity

data class AbilityDto(
	val slot: String,
	val displayName: String,
	val description: String,
	val displayIcon: String?,
) {
	fun toEntity(
		version: String,
		agentUuid: String,
	): AbilityEntity {
		return AbilityEntity(
			agentUuid + slot, agentUuid, version, slot, displayName, description, displayIcon
		)
	}
}