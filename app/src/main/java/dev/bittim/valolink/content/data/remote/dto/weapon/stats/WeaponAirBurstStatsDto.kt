/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponAirBurstStatsDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.remote.dto.weapon.stats

import dev.bittim.valolink.content.data.local.entity.weapon.stats.WeaponAirBurstStatsEntity

data class WeaponAirBurstStatsDto(
	val shotgunPelletCount: Int,
	val burstDistance: Double,
) {
	fun toEntity(
		version: String,
		weaponStatsUuid: String,
	): WeaponAirBurstStatsEntity {
		return WeaponAirBurstStatsEntity(
			uuid = weaponStatsUuid,
			version = version,
			weaponStats = weaponStatsUuid,
			shotgunPelletCount = shotgunPelletCount,
			burstDistance = burstDistance,
		)
	}
}