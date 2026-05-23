/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoWeaponStatsEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import dev.bittim.valolink.core.data.local.embedded.ValoWeaponStatsAdsStats
import dev.bittim.valolink.core.data.local.embedded.ValoWeaponStatsAirBurstStats
import dev.bittim.valolink.core.data.local.embedded.ValoWeaponStatsAltShotgunStats
import dev.bittim.valolink.core.data.local.embedded.ValoWeaponStatsDamageRange
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_weapon_stats",
    primaryKeys = ["weapon"],
    foreignKeys = [
        ForeignKey(
            entity = ValoWeaponEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["weapon"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
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