/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponStats.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:48
 */

package dev.bittim.valolink.content.domain.model.weapon.stats

data class WeaponStats(
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
    val adsStats: WeaponAdsStats?,
    val altShotgunStats: WeaponAltShotgunStats?,
    val airBurstStats: WeaponAirBurstStats?,
    val damageRanges: List<WeaponDamageRange>,
)
