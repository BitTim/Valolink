/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       EventDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.remote.dto

import dev.bittim.valolink.content.data.local.entity.EventEntity

data class EventDto(
	val uuid: String,
	val displayName: String,
	val shortDisplayName: String,
	val startTime: String,
	val endTime: String,
	val assetPath: String,
) {
	fun toEntity(version: String): EventEntity {
		return EventEntity(
			uuid, version, displayName, shortDisplayName, startTime, endTime, assetPath
		)
	}
}
