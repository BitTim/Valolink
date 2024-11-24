package dev.bittim.valolink.core.data.remote.game.dto

import dev.bittim.valolink.core.data.local.game.entity.CurrencyEntity

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