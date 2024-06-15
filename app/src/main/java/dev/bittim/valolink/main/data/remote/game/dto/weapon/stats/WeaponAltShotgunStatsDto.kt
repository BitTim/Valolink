package dev.bittim.valolink.main.data.remote.game.dto.weapon.stats

import dev.bittim.valolink.main.data.local.game.entity.weapon.stats.WeaponAltShotgunStatsEntity

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