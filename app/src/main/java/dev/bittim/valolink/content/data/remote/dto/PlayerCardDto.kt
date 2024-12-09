package dev.bittim.valolink.content.data.remote.dto

import dev.bittim.valolink.content.data.local.entity.PlayerCardEntity

data class PlayerCardDto(
	val uuid: String,
	val displayName: String,
	val isHiddenIfNotOwned: Boolean,
	val themeUuid: String?,
	val displayIcon: String,
	val smallArt: String,
	val wideArt: String,
	val largeArt: String?,
	val assetPath: String,
) {
	fun toEntity(version: String): PlayerCardEntity {
		return PlayerCardEntity(
			uuid = uuid,
			version = version,
			displayName = displayName,
			isHiddenIfNotOwned = isHiddenIfNotOwned,
			themeUuid = themeUuid,
			displayIcon = displayIcon,
			smallArt = smallArt,
			wideArt = wideArt,
			largeArt = largeArt
		)
	}
}