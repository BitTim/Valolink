/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponShopDataWithAllRelations.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.local.relation.weapon

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.data.local.entity.weapon.shopData.WeaponGridPositionEntity
import dev.bittim.valolink.content.data.local.entity.weapon.shopData.WeaponShopDataEntity
import dev.bittim.valolink.content.domain.model.weapon.shopData.WeaponShopData

data class WeaponShopDataWithAllRelations(
    @Embedded val weaponShopData: WeaponShopDataEntity,
    @Relation(
        parentColumn = "uuid", entityColumn = "weaponShopData"
    ) val gridPosition: WeaponGridPositionEntity?,
) : VersionedEntity {
    override val uuid: String
        get() = weaponShopData.uuid
    override val version: String
        get() = weaponShopData.version

    fun toType(): WeaponShopData {
        return weaponShopData.toType(
            gridPosition?.toType()
        )
    }
}
