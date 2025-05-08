/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       VersionDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.remote.dto

import dev.bittim.valolink.content.data.local.entity.VersionEntity

data class VersionDto(
	val manifestId: String,
	val branch: String,
	val version: String,
	val buildVersion: String,
	val engineVersion: String,
	val riotClientVersion: String,
	val riotClientBuild: String,
	val buildDate: String,
) {
	fun toEntity(): VersionEntity {
		return VersionEntity(
            uuid = "",
			manifestId = manifestId,
			branch = branch,
			version = version,
			buildVersion = buildVersion,
			engineVersion = engineVersion,
			riotClientVersion = riotClientVersion,
			riotClientBuild = riotClientBuild,
			buildDate = buildDate,
		)
	}
}
