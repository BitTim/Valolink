/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponShopDataEntity.kt
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
import dev.bittim.valolink.content.data.local.entity.weapon.WeaponEntity
import dev.bittim.valolink.content.domain.model.weapon.shopData.WeaponGridPosition
import dev.bittim.valolink.content.domain.model.weapon.shopData.WeaponShopData

@Entity(
    tableName = "WeaponShopData", foreignKeys = [ForeignKey(
        entity = WeaponEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["weapon"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(
        value = ["uuid"], unique = true
    ), Index(
        value = ["weapon"], unique = true
    )]
)
data class WeaponShopDataEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val weapon: String,
    val cost: Int,
    val category: String,
    val shopOrderPriority: Int,
    val categoryText: String,
    val canBeTrashed: Boolean,
    val image: String?,
    val newImage: String,
    val newImage2: String?,
) : VersionedEntity {
    fun toType(
        gridPosition: WeaponGridPosition?,
    ): WeaponShopData {
        return WeaponShopData(
            cost = cost,
            category = category,
            shopOrderPriority = shopOrderPriority,
            categoryText = categoryText,
            gridPosition = gridPosition,
            canBeTrashed = canBeTrashed,
            image = image,
            newImage = newImage,
            newImage2 = newImage2,
        )
    }
}
