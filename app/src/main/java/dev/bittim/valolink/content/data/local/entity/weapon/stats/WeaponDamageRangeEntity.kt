/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponDamageRangeEntity.kt
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
import dev.bittim.valolink.content.domain.model.weapon.stats.WeaponDamageRange

@Entity(
    tableName = "WeaponDamageRanges", foreignKeys = [ForeignKey(
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
data class WeaponDamageRangeEntity(
    @PrimaryKey override val uuid: String,
    override val version: String,
    val weaponStats: String,
    val rangeStartMeters: Double,
    val rangeEndMeters: Double,
    val headDamage: Double,
    val bodyDamage: Double,
    val legDamage: Double,
) : VersionedEntity {
    fun toType(): WeaponDamageRange {
        return WeaponDamageRange(
            rangeStartMeters = rangeStartMeters,
            rangeEndMeters = rangeEndMeters,
            headDamage = headDamage,
            bodyDamage = bodyDamage,
            legDamage = legDamage,
        )
    }
}
