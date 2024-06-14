package dev.bittim.valolink.main.domain.model.game.weapon.stats

data class WeaponStats(
    val fireRate: Float,
    val magazineSize: Int,
    val runSpeedMultiplier: Float,
    val equipTimeSeconds: Float,
    val reloadTimeSeconds: Float,
    val firstBulletAccuracy: Float,
    val shotgunPelletCount: Int,
    val wallPenetration: String,
    val feature: String?,
    val fireMode: String?,
    val altFireMode: String,
    val adsStats: WeaponAdsStats,
    val altShotgunStats: WeaponAltShotgunStats?,
    val airBurstStats: WeaponAirBurstStats?,
    val damageRanges: List<WeaponDamageRange>
)
