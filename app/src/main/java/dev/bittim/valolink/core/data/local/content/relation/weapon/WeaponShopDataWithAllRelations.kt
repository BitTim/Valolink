package dev.bittim.valolink.core.data.local.content.relation.weapon

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.core.data.local.content.entity.VersionedEntity
import dev.bittim.valolink.core.data.local.content.entity.weapon.shopData.WeaponGridPositionEntity
import dev.bittim.valolink.core.data.local.content.entity.weapon.shopData.WeaponShopDataEntity
import dev.bittim.valolink.main.domain.model.game.weapon.shopData.WeaponShopData

data class WeaponShopDataWithAllRelations(
	@Embedded val weaponShopData: WeaponShopDataEntity,
	@Relation(
		parentColumn = "uuid", entityColumn = "weaponShopData"
	) val gridPosition: WeaponGridPositionEntity?,
) : VersionedEntity {
	override fun getApiVersion(): String {
		return weaponShopData.version
	}

	fun toType(): WeaponShopData {
		return weaponShopData.toType(
			gridPosition?.toType()
		)
	}
}
