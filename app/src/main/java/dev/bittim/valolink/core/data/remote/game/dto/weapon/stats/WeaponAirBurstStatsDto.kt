package dev.bittim.valolink.core.data.remote.game.dto.weapon.stats

import dev.bittim.valolink.core.data.local.game.entity.weapon.stats.WeaponAirBurstStatsEntity

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