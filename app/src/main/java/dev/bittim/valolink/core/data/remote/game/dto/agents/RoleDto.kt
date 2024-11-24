package dev.bittim.valolink.core.data.remote.game.dto.agents

import dev.bittim.valolink.core.data.local.game.entity.agent.RoleEntity

data class RoleDto(
	val uuid: String,
	val displayName: String,
	val description: String,
	val displayIcon: String,
	val assetPath: String,
) {
	fun toEntity(version: String): RoleEntity {
		return RoleEntity(
			uuid, version, displayName, description, displayIcon, assetPath
		)
	}
}