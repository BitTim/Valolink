package dev.bittim.valolink.content.data.remote.dto

import dev.bittim.valolink.content.data.local.entity.CurrencyEntity

data class CurrencyDto(
	val uuid: String,
	val displayName: String,
	val displayNameSingular: String,
	val displayIcon: String,
	val largeIcon: String,
	val assetPath: String,
) {
	fun toEntity(version: String): CurrencyEntity {
		return CurrencyEntity(
			uuid, version, displayName, displayNameSingular, displayIcon, largeIcon
		)
	}
}