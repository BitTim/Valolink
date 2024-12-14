/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponAltShotgunStatsDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.data.remote.dto.weapon.stats

import dev.bittim.valolink.content.data.local.entity.weapon.stats.WeaponAltShotgunStatsEntity

data class WeaponAltShotgunStatsDto(
	val shotgunPelletCount: Int,
	val burstRate: Double,
) {
	fun toEntity(
		version: String,
		weaponStatsUuid: String,
	): WeaponAltShotgunStatsEntity {
		return WeaponAltShotgunStatsEntity(
			uuid = weaponStatsUuid,
			version = version,
			weaponStats = weaponStatsUuid,
			shotgunPelletCount = shotgunPelletCount,
			burstRate = burstRate,
		)
	}
}