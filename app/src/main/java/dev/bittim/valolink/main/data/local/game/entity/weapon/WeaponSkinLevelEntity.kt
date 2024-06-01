package dev.bittim.valolink.main.data.local.game.entity.weapon

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.main.data.local.game.entity.GameEntity
import dev.bittim.valolink.main.domain.model.game.weapon.WeaponSkinLevel

@Entity(
    tableName = "WeaponSkinLevels",
    indices = [Index(
        value = ["uuid"],
        unique = true
    )]
)
data class WeaponSkinLevelEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val displayName: String,
    val levelItem: String?,
    val displayIcon: String,
    val streamedVideo: String?,
) : GameEntity() {
    fun toType(): WeaponSkinLevel {
        return WeaponSkinLevel(
            uuid = uuid,
            displayName = displayName,
            levelItem = levelItem,
            displayIcon = displayIcon,
            streamedVideo = streamedVideo
        )
    }
}
