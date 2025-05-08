/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponStatsDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.remote.dto.weapon.stats

import dev.bittim.valolink.content.data.local.entity.weapon.stats.WeaponStatsEntity

data class WeaponStatsDto(
	val fireRate: Double,
	val magazineSize: Int,
	val runSpeedMultiplier: Double,
	val equipTimeSeconds: Double,
	val reloadTimeSeconds: Double,
	val firstBulletAccuracy: Double,
	val shotgunPelletCount: Int,
	val wallPenetration: String,
	val feature: String?,
	val fireMode: String?,
	val altFireType: String?,
	val adsStats: WeaponAdsStatsDto?,
	val altShotgunStats: WeaponAltShotgunStatsDto?,
	val airBurstStats: WeaponAirBurstStatsDto?,
	val damageRanges: List<WeaponDamageRangeDto>,
) {
	fun toEntity(
		version: String,
		weaponUuid: String,
	): WeaponStatsEntity {
		return WeaponStatsEntity(
			uuid = weaponUuid,
			version = version,
			weapon = weaponUuid,
			fireRate = fireRate,
			magazineSize = magazineSize,
			runSpeedMultiplier = runSpeedMultiplier,
			equipTimeSeconds = equipTimeSeconds,
			reloadTimeSeconds = reloadTimeSeconds,
			firstBulletAccuracy = firstBulletAccuracy,
			shotgunPelletCount = shotgunPelletCount,
			wallPenetration = wallPenetration,
			feature = feature,
			fireMode = fireMode,
			altFireType = altFireType,
		)
	}
}