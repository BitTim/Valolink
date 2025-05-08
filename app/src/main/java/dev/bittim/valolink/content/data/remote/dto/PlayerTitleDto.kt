/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       PlayerTitleDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.remote.dto

import dev.bittim.valolink.content.data.local.entity.PlayerTitleEntity

data class PlayerTitleDto(
	val uuid: String,
	val displayName: String?,
	val titleText: String?,
	val isHiddenIfNotOwned: Boolean,
	val assetPath: String,
) {
	fun toEntity(version: String): PlayerTitleEntity {
		return PlayerTitleEntity(
			uuid = uuid,
			version = version,
			displayName = displayName,
			titleText = titleText,
			isHiddenIfNotOwned = isHiddenIfNotOwned
		)
	}
}