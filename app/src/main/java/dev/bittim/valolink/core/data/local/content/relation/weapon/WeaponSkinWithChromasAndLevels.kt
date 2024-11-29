package dev.bittim.valolink.core.data.local.content.relation.weapon

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.core.data.local.content.entity.VersionedEntity
import dev.bittim.valolink.core.data.local.content.entity.weapon.skins.WeaponSkinChromaEntity
import dev.bittim.valolink.core.data.local.content.entity.weapon.skins.WeaponSkinEntity
import dev.bittim.valolink.core.data.local.content.entity.weapon.skins.WeaponSkinLevelEntity
import dev.bittim.valolink.main.domain.model.game.weapon.skins.WeaponSkin

data class WeaponSkinWithChromasAndLevels(
	@Embedded val weaponSkin: WeaponSkinEntity,
	@Relation(
		parentColumn = "uuid", entityColumn = "weaponSkin"
	) val chromas: Set<WeaponSkinChromaEntity>,
	@Relation(
		parentColumn = "uuid", entityColumn = "weaponSkin"
	) val levels: Set<WeaponSkinLevelEntity>,
) : VersionedEntity {
	override fun getApiVersion(): String {
		return weaponSkin.version
	}

	fun toType(): WeaponSkin {
		return weaponSkin.toType(chromas = chromas.map { it.toType() }.sortedBy { it.chromaIndex },
								 levels = levels.map { it.toType() }.sortedBy { it.levelIndex })
	}
}
