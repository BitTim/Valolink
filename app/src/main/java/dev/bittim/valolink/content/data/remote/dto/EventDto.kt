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
