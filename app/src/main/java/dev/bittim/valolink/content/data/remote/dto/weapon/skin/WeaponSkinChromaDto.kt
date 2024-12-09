package dev.bittim.valolink.content.data.remote.dto.weapon.skin

import dev.bittim.valolink.content.data.local.entity.weapon.skins.WeaponSkinChromaEntity

data class WeaponSkinChromaDto(
	val uuid: String,
	val displayName: String,
	val displayIcon: String?,
	val fullRender: String,
	val swatch: String?,
	val streamedVideo: String?,
	val assetPath: String,
) {
	fun toEntity(
		version: String,
		skinUuid: String,
		chromaIndex: Int,
	): WeaponSkinChromaEntity {
		return WeaponSkinChromaEntity(
			uuid = uuid,
			version = version,
			weaponSkin = skinUuid,
			chromaIndex = chromaIndex,
			displayName = displayName,
			displayIcon = displayIcon,
			fullRender = fullRender,
			swatch = swatch,
			streamedVideo = streamedVideo,
		)
	}
}