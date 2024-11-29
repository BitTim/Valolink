package dev.bittim.valolink.core.data.remote.content.dto.buddy

import dev.bittim.valolink.core.data.local.content.entity.buddy.BuddyEntity

data class BuddyDto(
	val uuid: String,
	val displayName: String,
	val isHiddenIfNotOwned: Boolean,
	val themeUuid: String?,
	val displayIcon: String,
	val assetPath: String,
	val levels: List<BuddyLevelDto>,
) {
	fun toEntity(version: String): BuddyEntity {
		return BuddyEntity(
			uuid = uuid,
			version = version,
			displayName = displayName,
			isHiddenIfNotOwned = isHiddenIfNotOwned,
			themeUuid = themeUuid,
			displayIcon = displayIcon,
			assetPath = assetPath
		)
	}
}