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