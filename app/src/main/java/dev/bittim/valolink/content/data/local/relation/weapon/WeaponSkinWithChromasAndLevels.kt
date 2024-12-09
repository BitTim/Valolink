package dev.bittim.valolink.content.data.local.relation.weapon

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.data.local.entity.weapon.skins.WeaponSkinChromaEntity
import dev.bittim.valolink.content.data.local.entity.weapon.skins.WeaponSkinEntity
import dev.bittim.valolink.content.data.local.entity.weapon.skins.WeaponSkinLevelEntity
import dev.bittim.valolink.content.domain.model.weapon.skins.WeaponSkin

data class WeaponSkinWithChromasAndLevels(
    @Embedded val weaponSkin: WeaponSkinEntity,
    @Relation(
        parentColumn = "uuid", entityColumn = "weaponSkin"
    ) val chromas: Set<WeaponSkinChromaEntity>,
    @Relation(
        parentColumn = "uuid", entityColumn = "weaponSkin"
    ) val levels: Set<WeaponSkinLevelEntity>,
) : VersionedEntity {
    override val version: String
        get() = weaponSkin.version

    fun toType(): WeaponSkin {
        return weaponSkin.toType(chromas = chromas.map { it.toType() }.sortedBy { it.chromaIndex },
            levels = levels.map { it.toType() }.sortedBy { it.levelIndex })
    }
}
