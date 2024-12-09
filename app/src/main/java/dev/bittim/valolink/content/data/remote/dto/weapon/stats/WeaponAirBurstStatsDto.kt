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