/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       PlayerCardDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

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