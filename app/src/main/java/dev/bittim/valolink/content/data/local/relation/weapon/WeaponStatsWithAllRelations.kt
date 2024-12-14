/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponStatsWithAllRelations.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.data.local.relation.weapon

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.data.local.entity.weapon.stats.WeaponAdsStatsEntity
import dev.bittim.valolink.content.data.local.entity.weapon.stats.WeaponAirBurstStatsEntity
import dev.bittim.valolink.content.data.local.entity.weapon.stats.WeaponAltShotgunStatsEntity
import dev.bittim.valolink.content.data.local.entity.weapon.stats.WeaponDamageRangeEntity
import dev.bittim.valolink.content.data.local.entity.weapon.stats.WeaponStatsEntity
import dev.bittim.valolink.content.domain.model.weapon.stats.WeaponStats

data class WeaponStatsWithAllRelations(
    @Embedded val weaponStats: WeaponStatsEntity,
    @Relation(
        parentColumn = "uuid", entityColumn = "weaponStats"
    ) val weaponAdsStats: WeaponAdsStatsEntity?,
    @Relation(
        parentColumn = "uuid", entityColumn = "weaponStats"
    ) val weaponAltShotgunStats: WeaponAltShotgunStatsEntity?,
    @Relation(
        parentColumn = "uuid", entityColumn = "weaponStats"
    ) val weaponAirBurstStats: WeaponAirBurstStatsEntity?,
    @Relation(
        parentColumn = "uuid", entityColumn = "weaponStats"
    ) val weaponDamageRanges: Set<WeaponDamageRangeEntity>,
) : VersionedEntity {
    override val version: String
        get() = weaponStats.version

    fun toType(): WeaponStats {
        return weaponStats.toType(adsStats = weaponAdsStats?.toType(),
            altShotgunStats = weaponAltShotgunStats?.toType(),
            airBurstStats = weaponAirBurstStats?.toType(),
            damageRanges = weaponDamageRanges.map { it.toType() })
    }
}
