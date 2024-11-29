package dev.bittim.valolink.core.data.remote.content.dto.weapon.stats

import dev.bittim.valolink.core.data.local.content.entity.weapon.stats.WeaponAltShotgunStatsEntity

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