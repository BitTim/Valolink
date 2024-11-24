package dev.bittim.valolink.core.data.remote.game.dto.weapon.stats

import dev.bittim.valolink.core.data.local.game.entity.weapon.stats.WeaponStatsEntity

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