/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponSkinLevelDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.remote.dto.weapon.skin

import dev.bittim.valolink.content.data.local.entity.weapon.skins.WeaponSkinLevelEntity

data class WeaponSkinLevelDto(
	val uuid: String,
	val displayName: String,
	val levelItem: String?,
	val displayIcon: String?,
	val streamedVideo: String?,
	val assetPath: String,
) {
	fun toEntity(
		version: String,
		skinUuid: String,
		levelIndex: Int,
	): WeaponSkinLevelEntity {
		return WeaponSkinLevelEntity(
			uuid = uuid,
			version = version,
			weaponSkin = skinUuid,
			levelIndex = levelIndex,
			displayName = displayName,
			levelItem = levelItem,
			displayIcon = displayIcon,
			streamedVideo = streamedVideo,
		)
	}
}