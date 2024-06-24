package dev.bittim.valolink.main.data.local.game.relation.weapon

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.main.data.local.game.entity.VersionedEntity
import dev.bittim.valolink.main.data.local.game.entity.weapon.skins.WeaponSkinChromaEntity
import dev.bittim.valolink.main.data.local.game.entity.weapon.skins.WeaponSkinEntity
import dev.bittim.valolink.main.data.local.game.entity.weapon.skins.WeaponSkinLevelEntity
import dev.bittim.valolink.main.domain.model.game.weapon.skins.WeaponSkin

data class WeaponSkinWithChromasAndLevels(
    @Embedded val weaponSkin: WeaponSkinEntity,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "weaponSkin"
    ) val chromas: Set<WeaponSkinChromaEntity>,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "weaponSkin"
    ) val levels: Set<WeaponSkinLevelEntity>,
) : VersionedEntity {
    override fun getApiVersion(): String {
        return weaponSkin.version
    }

    fun toType(): WeaponSkin {
        return weaponSkin.toType(chromas = chromas.map { it.toType() },
                                 levels = levels.map { it.toType() })
    }
}
