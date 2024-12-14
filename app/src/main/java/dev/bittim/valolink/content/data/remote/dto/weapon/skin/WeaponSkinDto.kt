/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponSkinDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.data.remote.dto.weapon.skin

import dev.bittim.valolink.content.data.local.entity.weapon.skins.WeaponSkinEntity

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