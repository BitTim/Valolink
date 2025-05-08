/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       RoleDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.remote.dto.agents

import dev.bittim.valolink.content.data.local.entity.agent.RoleEntity

data class RoleDto(
	val uuid: String,
	val displayName: String,
	val description: String,
	val displayIcon: String,
	val assetPath: String,
) {
	fun toEntity(version: String): RoleEntity {
		return RoleEntity(
			uuid, version, displayName, description, displayIcon, assetPath
		)
	}
}