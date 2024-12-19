/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponWithAllRelations.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.local.relation.weapon

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.data.local.entity.weapon.WeaponEntity
import dev.bittim.valolink.content.data.local.entity.weapon.shopData.WeaponShopDataEntity
import dev.bittim.valolink.content.data.local.entity.weapon.skins.WeaponSkinEntity
import dev.bittim.valolink.content.data.local.entity.weapon.stats.WeaponStatsEntity
import dev.bittim.valolink.content.domain.model.weapon.Weapon

data class WeaponWithAllRelations(
    @Embedded val weapon: WeaponEntity,
    @Relation(
        entity = WeaponStatsEntity::class, parentColumn = "uuid", entityColumn = "weapon"
    ) val weaponStats: WeaponStatsWithAllRelations?,
    @Relation(
        entity = WeaponShopDataEntity::class, parentColumn = "uuid", entityColumn = "weapon"
    ) val shopData: WeaponShopDataWithAllRelations?,
    @Relation(
        entity = WeaponSkinEntity::class, parentColumn = "uuid", entityColumn = "weapon"
    ) val skins: Set<WeaponSkinWithChromasAndLevels>,
) : VersionedEntity {
    override val uuid: String
        get() = weapon.uuid
    override val version: String
        get() = weapon.version

    fun toType(): Weapon {
        return weapon.toType(weaponStats = weaponStats?.toType(),
            shopData = shopData?.toType(),
            skins = skins.map { it.toType() })
    }
}
