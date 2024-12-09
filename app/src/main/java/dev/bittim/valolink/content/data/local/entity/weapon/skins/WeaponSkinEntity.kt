package dev.bittim.valolink.content.data.local.entity.weapon.skins

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.data.local.entity.weapon.WeaponEntity
import dev.bittim.valolink.content.domain.model.weapon.skins.WeaponSkin
import dev.bittim.valolink.content.domain.model.weapon.skins.WeaponSkinChroma
import dev.bittim.valolink.content.domain.model.weapon.skins.WeaponSkinLevel

@Entity(
    tableName = "WeaponSkins", foreignKeys = [ForeignKey(
        entity = WeaponEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["weapon"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(
        value = ["uuid"], unique = true
    ), Index(
        value = ["weapon"], unique = false
    )]
)
data class WeaponSkinEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val weapon: String,
    val displayName: String,
    val themeUuid: String,
    val contentTierUuid: String?,
    val displayIcon: String?,
    val wallpaper: String?,
) : VersionedEntity {
    fun toType(
        chromas: List<WeaponSkinChroma>,
        levels: List<WeaponSkinLevel>,
    ): WeaponSkin {
        return WeaponSkin(
            uuid = uuid,
            displayName = displayName,
            themeUuid = themeUuid,
            contentTierUuid = contentTierUuid,
            displayIcon = displayIcon,
            wallpaper = wallpaper,
            chromas = chromas,
            levels = levels
        )
    }
}
