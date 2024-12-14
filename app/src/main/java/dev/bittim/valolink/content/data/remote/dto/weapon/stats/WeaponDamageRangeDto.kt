/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponDamageRangeDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.data.remote.dto.weapon.stats

import dev.bittim.valolink.content.data.local.entity.weapon.stats.WeaponDamageRangeEntity

data class WeaponDamageRangeDto(
	val rangeStartMeters: Double,
	val rangeEndMeters: Double,
	val headDamage: Double,
	val bodyDamage: Double,
	val legDamage: Double,
) {
	fun toEntity(
		version: String,
		weaponStatsUuid: String,
	): WeaponDamageRangeEntity {
		return WeaponDamageRangeEntity(
			uuid = weaponStatsUuid + "_" + rangeStartMeters + "-" + rangeEndMeters,
			version = version,
			weaponStats = weaponStatsUuid,
			rangeStartMeters = rangeStartMeters,
			rangeEndMeters = rangeEndMeters,
			headDamage = headDamage,
			bodyDamage = bodyDamage,
			legDamage = legDamage,
		)
	}
}