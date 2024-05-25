package dev.bittim.valolink.feature.content.data.local.game.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.domain.model.game.PlayerTitle

@Entity(
    tableName = "PlayerTitles", indices = [Index(value = ["uuid"], unique = true)]
)
data class PlayerTitleEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val displayName: String,
    val titleText: String,
    val isHiddenIfNotOwned: Boolean,
) : GameEntity() {
    fun toType(): PlayerTitle {
        return PlayerTitle(
            uuid = uuid,
            displayName = displayName,
            titleText = titleText,
            isHiddenIfNotOwned = isHiddenIfNotOwned,
        )
    }
}
