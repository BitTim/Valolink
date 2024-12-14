/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       SeasonDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.data.remote.dto

import dev.bittim.valolink.content.data.local.entity.SeasonEntity

data class SeasonDto(
	val uuid: String,
	val displayName: String,
	val type: String?,
	val startTime: String,
	val endTime: String,
	val parentUuid: String?,
	val assetPath: String,
) {
	fun toEntity(version: String): SeasonEntity {
		return SeasonEntity(
			uuid = uuid,
			version = version,
			displayName = displayName,
			type = type,
			startTime = startTime,
			endTime = endTime,
			parentUuid = parentUuid
		)
	}
}