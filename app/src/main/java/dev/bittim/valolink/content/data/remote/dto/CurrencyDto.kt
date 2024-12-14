/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       CurrencyDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

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