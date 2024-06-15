package dev.bittim.valolink.main.data.remote.game.dto.weapon.stats

import dev.bittim.valolink.main.data.local.game.entity.weapon.stats.WeaponAdsStatsEntity

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