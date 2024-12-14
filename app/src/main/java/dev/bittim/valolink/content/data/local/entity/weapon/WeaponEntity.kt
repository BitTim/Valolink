/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.data.local.entity.weapon

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.domain.model.weapon.Weapon
import dev.bittim.valolink.content.domain.model.weapon.shopData.WeaponShopData
import dev.bittim.valolink.content.domain.model.weapon.skins.WeaponSkin
import dev.bittim.valolink.content.domain.model.weapon.stats.WeaponStats

@Entity(
    tableName = "Weapons", indices = [Index(
        value = ["uuid"], unique = true
    )]
)
data class WeaponEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val displayName: String,
    val category: String,
    val defaultSkinUuid: String,
    val displayIcon: String,
    val killStreamIcon: String,
) : VersionedEntity {
    fun toType(
        weaponStats: WeaponStats?,
        shopData: WeaponShopData?,
        skins: List<WeaponSkin>,
    ): Weapon {
        return Weapon(
            uuid = uuid,
            displayName = displayName,
            category = category,
            defaultSkinUuid = defaultSkinUuid,
            displayIcon = displayIcon,
            killStreamIcon = killStreamIcon,
            weaponStats = weaponStats,
            shopData = shopData,
            skins = skins
        )
    }
}
