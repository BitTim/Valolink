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
    override val version: String
        get() = weaponShopData.version

    fun toType(): WeaponShopData {
        return weaponShopData.toType(
            gridPosition?.toType()
        )
    }
}
