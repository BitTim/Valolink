package dev.bittim.valolink.core.data.remote.game.dto.buddy

import dev.bittim.valolink.core.data.local.game.entity.buddy.BuddyLevelEntity

data class BuddyLevelDto(
	val uuid: String,
	val charmLevel: Int,
	val hideIfNotOwned: Boolean,
	val displayName: String,
	val displayIcon: String,
	val assetPath: String,
) {
	fun toEntity(
		version: String,
		buddy: String,
	): BuddyLevelEntity {
		return BuddyLevelEntity(
			uuid = uuid,
			version = version,
			buddy = buddy,
			hideIfNotOwned = hideIfNotOwned,
			displayName = displayName,
			displayIcon = displayIcon
		)
	}
}