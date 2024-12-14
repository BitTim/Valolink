/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       BuddyLevelDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.data.remote.dto.buddy

import dev.bittim.valolink.content.data.local.entity.buddy.BuddyLevelEntity

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