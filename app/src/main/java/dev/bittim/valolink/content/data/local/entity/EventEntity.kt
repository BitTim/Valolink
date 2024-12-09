package dev.bittim.valolink.content.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.domain.model.Event
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