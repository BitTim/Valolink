package dev.bittim.valolink.main.data.local.game.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.main.domain.model.game.Event
import java.time.Instant

@Entity(
    tableName = "Events",
    indices = [Index(
        value = ["uuid"],
        unique = true
    )]
)
data class EventEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val displayName: String,
    val shortDisplayName: String,
    val startTime: String,
    val endTime: String,
    val assetPath: String,
) : GameEntity() {
    fun toType(): Event {
        return Event(
            uuid,
            displayName,
            shortDisplayName,
            Instant.parse(startTime),
            Instant.parse(endTime)
        )
    }
}