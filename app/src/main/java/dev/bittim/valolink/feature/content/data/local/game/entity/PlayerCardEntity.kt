package dev.bittim.valolink.feature.content.data.local.game.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.domain.model.PlayerCard

@Entity(
    tableName = "PlayerCards", indices = [Index(value = ["uuid"], unique = true)]
)
data class PlayerCardEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val displayName: String,
    val isHiddenIfNotOwned: Boolean,
    val themeUuid: String?,
    val displayIcon: String,
    val smallArt: String,
    val wideArt: String,
    val largeArt: String
) : GameEntity() {
    fun toType(): PlayerCard {
        return PlayerCard(
            uuid = uuid,
            displayName = displayName,
            isHiddenIfNotOwned = isHiddenIfNotOwned,
            themeUuid = themeUuid,
            displayIcon = displayIcon,
            smallArt = smallArt,
            wideArt = wideArt,
            largeArt = largeArt
        )
    }
}
