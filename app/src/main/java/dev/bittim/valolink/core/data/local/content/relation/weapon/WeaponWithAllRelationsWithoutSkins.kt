package dev.bittim.valolink.core.data.local.content.relation.weapon

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.core.data.local.content.entity.VersionedEntity
import dev.bittim.valolink.core.data.local.content.entity.weapon.WeaponEntity
import dev.bittim.valolink.core.data.local.content.entity.weapon.shopData.WeaponShopDataEntity
import dev.bittim.valolink.core.data.local.content.entity.weapon.stats.WeaponStatsEntity
import dev.bittim.valolink.main.domain.model.game.weapon.Weapon

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
	override fun getApiVersion(): String {
		return weapon.version
	}

	fun toType(): Weapon {
		return weapon.toType(
			weaponStats = weaponStats.toType(), shopData = shopData.toType(), skins = emptyList()
		)
	}
}
