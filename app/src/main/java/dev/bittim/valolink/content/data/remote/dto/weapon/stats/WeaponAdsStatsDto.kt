/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponAdsStatsDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.remote.dto.weapon.stats

import dev.bittim.valolink.content.data.local.entity.weapon.stats.WeaponAdsStatsEntity

data class WeaponAdsStatsDto(
	val zoomMultiplier: Double,
	val fireRate: Double,
	val runSpeedMultiplier: Double,
	val burstCount: Int,
	val firstBulletAccuracy: Double,
) {
	fun toEntity(
		version: String,
		weaponStatsUuid: String,
	): WeaponAdsStatsEntity {
		return WeaponAdsStatsEntity(
			uuid = weaponStatsUuid,
			version = version,
			weaponStats = weaponStatsUuid,
			zoomMultiplier = zoomMultiplier,
			fireRate = fireRate,
			runSpeedMultiplier = runSpeedMultiplier,
			burstCount = burstCount,
			firstBulletAccuracy = firstBulletAccuracy,
		)
	}
}