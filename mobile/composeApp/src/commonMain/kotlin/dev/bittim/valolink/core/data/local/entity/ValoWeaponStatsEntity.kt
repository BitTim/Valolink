/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoWeaponStatsEntity.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.05.26, 02:39
 */

package dev.bittim.valolink.core.data.local.entity

import dev.bittim.valolink.core.domain.model.ValoWeaponStatsAdsStats
import dev.bittim.valolink.core.domain.model.ValoWeaponStatsAirBurstStats
import dev.bittim.valolink.core.domain.model.ValoWeaponStatsAltShotgunStats
import dev.bittim.valolink.core.domain.model.ValoWeaponStatsDamageRange
import kotlin.uuid.Uuid

data class ValoWeaponStatsEntity(
    val weapon: Uuid,
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
    val altFireType: String?,
    val adsStats: ValoWeaponStatsAdsStats?,
    val altShotgunStats: ValoWeaponStatsAltShotgunStats?,
    val airBurstStats: ValoWeaponStatsAirBurstStats?,
    val damageRanges: List<ValoWeaponStatsDamageRange>
)