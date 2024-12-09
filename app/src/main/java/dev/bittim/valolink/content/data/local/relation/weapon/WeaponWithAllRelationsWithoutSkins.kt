package dev.bittim.valolink.content.data.local.relation.weapon

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.data.local.entity.weapon.WeaponEntity
import dev.bittim.valolink.content.data.local.entity.weapon.shopData.WeaponShopDataEntity
import dev.bittim.valolink.content.data.local.entity.weapon.stats.WeaponStatsEntity
import dev.bittim.valolink.content.domain.model.weapon.Weapon

// This one is without skins
data class WeaponWithAllRelationsWithoutSkins(
    @Embedded val weapon: WeaponEntity,
    @Relation(
        entity = WeaponStatsEntity::class, parentColumn = "uuid", entityColumn = "weapon"
    ) val weaponStats: WeaponStatsWithAllRelations,
    @Relation(
        entity = WeaponShopDataEntity::class, parentColumn = "uuid", entityColumn = "weapon"
    ) val shopData: WeaponShopDataWithAllRelations,
) : VersionedEntity {
    override val version: String
        get() = weapon.version

    fun toType(): Weapon {
        return weapon.toType(
            weaponStats = weaponStats.toType(), shopData = shopData.toType(), skins = emptyList()
        )
    }
}
