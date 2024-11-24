package dev.bittim.valolink.core.data.remote.game.dto.weapon.skin

import dev.bittim.valolink.core.data.local.game.entity.weapon.skins.WeaponSkinEntity

data class WeaponSkinDto(
	val uuid: String,
	val displayName: String,
	val themeUuid: String,
	val contentTierUuid: String?,
	val displayIcon: String?,
	val wallpaper: String?,
	val assetPath: String,
	val chromas: List<WeaponSkinChromaDto>,
	val levels: List<WeaponSkinLevelDto>,
) {
	fun toEntity(
		version: String,
		weaponUuid: String,
	): WeaponSkinEntity {
		return WeaponSkinEntity(
			uuid = uuid,
			version = version,
			weapon = weaponUuid,
			displayName = displayName,
			themeUuid = themeUuid,
			contentTierUuid = contentTierUuid,
			displayIcon = displayIcon,
			wallpaper = wallpaper
		)
	}
}