package dev.bittim.valolink.core.data.local.content.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.main.domain.model.game.Spray

@Entity(
    tableName = "Sprays",
    indices = [Index(
        value = ["uuid"],
        unique = true
    )]
)
data class SprayEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val displayName: String,
    val themeUuid: String?,
    val hideIfNotOwned: Boolean,
    val displayIcon: String,
    val fullIcon: String?,
    val fullTransparentIcon: String?,
    val animationPng: String?,
    val animationGif: String?,
) : GameEntity() {
    fun toType(): Spray {
        return Spray(
            uuid = uuid,
            displayName = displayName,
            themeUuid = themeUuid,
            hideIfNotOwned = hideIfNotOwned,
            displayIcon = displayIcon,
            fullIcon = fullIcon,
            fullTransparentIcon = fullTransparentIcon,
            animationPng = animationPng,
            animationGif = animationGif
        )
    }
}
