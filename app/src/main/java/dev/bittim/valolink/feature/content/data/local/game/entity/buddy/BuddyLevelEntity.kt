package dev.bittim.valolink.feature.content.data.local.game.entity.buddy

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.data.local.game.entity.GameEntity
import dev.bittim.valolink.feature.content.domain.model.game.buddy.BuddyLevel

@Entity(
    tableName = "BuddyLevels", indices = [Index(value = ["uuid"], unique = true)]
)
data class BuddyLevelEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val hideIfNotOwned: Boolean,
    val displayName: String,
    val displayIcon: String
) : GameEntity() {
    fun toType(): BuddyLevel {
        return BuddyLevel(
            uuid = uuid,
            hideIfNotOwned = hideIfNotOwned,
            displayName = displayName,
            displayIcon = displayIcon
        )
    }
}
