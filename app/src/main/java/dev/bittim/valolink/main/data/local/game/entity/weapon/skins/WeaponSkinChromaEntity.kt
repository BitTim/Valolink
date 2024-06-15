package dev.bittim.valolink.main.data.local.game.entity.weapon.skins

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.main.data.local.game.entity.GameEntity
import dev.bittim.valolink.main.domain.model.game.weapon.skins.WeaponSkinChroma

@Entity(
    tableName = "WeaponSkinChromas",
    foreignKeys = [
        ForeignKey(
            entity = WeaponSkinEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["weaponSkin"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            value = ["uuid"],
            unique = true
        ),
        Index(
            value = ["weaponSkin"],
            unique = false
        )
    ]
)
data class WeaponSkinChromaEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val weaponSkin: String,
    val displayName: String,
    val displayIcon: String?,
    val fullRender: String,
    val swatch: String?,
    val streamedVideo: String?,
) : GameEntity() {
    fun toType(): WeaponSkinChroma {
        return WeaponSkinChroma(
            uuid = uuid,
            displayName = displayName,
            displayIcon = displayIcon,
            fullRender = fullRender,
            swatch = swatch,
            streamedVideo = streamedVideo,
        )
    }
}
