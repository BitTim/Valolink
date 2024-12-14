/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       AbilityDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.data.remote.dto.agents

import dev.bittim.valolink.content.data.local.entity.agent.AbilityEntity

data class AbilityDto(
	val slot: String,
	val displayName: String,
	val description: String,
	val displayIcon: String?,
) {
	fun toEntity(
		version: String,
		agentUuid: String,
	): AbilityEntity {
		return AbilityEntity(
			agentUuid + slot, agentUuid, version, slot, displayName, description, displayIcon
		)
	}
}