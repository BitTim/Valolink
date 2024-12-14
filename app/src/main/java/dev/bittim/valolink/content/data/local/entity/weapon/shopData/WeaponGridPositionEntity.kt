/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponGridPositionEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.data.local.entity.weapon.shopData

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.domain.model.weapon.shopData.WeaponGridPosition

@Entity(
    tableName = "WeaponGridPositions", foreignKeys = [ForeignKey(
        entity = WeaponShopDataEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["weaponShopData"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(
        value = ["uuid"], unique = true
    ), Index(
        value = ["weaponShopData"], unique = true
    )]
)
data class WeaponGridPositionEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val weaponShopData: String,
    val row: Int,
    val column: Int,
) : VersionedEntity {
    fun toType(): WeaponGridPosition {
        return WeaponGridPosition(
            row = row,
            column = column,
        )
    }
}
