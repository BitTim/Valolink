/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponAdsStatsEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.local.entity.weapon.stats

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.domain.model.weapon.stats.WeaponAdsStats

@Entity(
    tableName = "WeaponAdsStats", foreignKeys = [ForeignKey(
        entity = WeaponStatsEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["weaponStats"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(
        value = ["uuid"], unique = true
    ), Index(
        value = ["weaponStats"], unique = true
    )]
)
data class WeaponAdsStatsEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val weaponStats: String,
    val zoomMultiplier: Double,
    val fireRate: Double,
    val runSpeedMultiplier: Double,
    val burstCount: Int,
    val firstBulletAccuracy: Double,
) : VersionedEntity {
    fun toType(): WeaponAdsStats {
        return WeaponAdsStats(
            zoomMultiplier = zoomMultiplier,
            fireRate = fireRate,
            runSpeedMultiplier = runSpeedMultiplier,
            burstCount = burstCount,
            firstBulletAccuracy = firstBulletAccuracy,
        )
    }
}