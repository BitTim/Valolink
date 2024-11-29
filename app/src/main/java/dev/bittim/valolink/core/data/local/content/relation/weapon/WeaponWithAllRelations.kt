package dev.bittim.valolink.core.data.local.content.relation.weapon

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.core.data.local.content.entity.VersionedEntity
import dev.bittim.valolink.core.data.local.content.entity.weapon.WeaponEntity
import dev.bittim.valolink.core.data.local.content.entity.weapon.shopData.WeaponShopDataEntity
import dev.bittim.valolink.core.data.local.content.entity.weapon.skins.WeaponSkinEntity
import dev.bittim.valolink.core.data.local.content.entity.weapon.stats.WeaponStatsEntity
import dev.bittim.valolink.main.domain.model.game.weapon.Weapon

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
	override fun getApiVersion(): String {
		return weapon.version
	}

	fun toType(): Weapon {
		return weapon.toType(weaponStats = weaponStats?.toType(),
							 shopData = shopData?.toType(),
							 skins = skins.map { it.toType() })
	}
}
