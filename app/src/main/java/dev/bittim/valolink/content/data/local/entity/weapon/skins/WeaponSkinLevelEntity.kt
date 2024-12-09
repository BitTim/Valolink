package dev.bittim.valolink.content.data.local.entity.weapon.skins

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.domain.model.weapon.skins.WeaponSkinLevel

@Entity(
    tableName = "WeaponSkinLevels", foreignKeys = [ForeignKey(
        entity = WeaponSkinEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["weaponSkin"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(
        value = ["uuid"], unique = true
    ), Index(
        value = ["weaponSkin"], unique = false
    )]
)
data class WeaponSkinLevelEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val weaponSkin: String,
    val levelIndex: Int,
    val displayName: String,
    val levelItem: String?,
    val displayIcon: String?,
    val streamedVideo: String?,
) : VersionedEntity {
    fun toType(): WeaponSkinLevel {
        return WeaponSkinLevel(
            uuid = uuid,
            levelIndex = levelIndex,
            displayName = displayName,
            levelItem = levelItem,
            displayIcon = displayIcon,
            streamedVideo = streamedVideo
        )
    }
}
