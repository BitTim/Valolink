package dev.bittim.valolink.feature.content.data.local.game.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.domain.model.Event
import java.time.ZonedDateTime

@Entity(
    tableName = "Events", indices = [Index(
        value = ["uuid"], unique = true
    )]
)
data class EventEntity(
    @PrimaryKey
    val uuid: String,
    override val version: String,
    val displayName: String,
    val shortDisplayName: String,
    val startTime: String,
    val endTime: String,
    val assetPath: String
) : GameEntity() {
    fun toType(): Event {
        return Event(
            uuid,
            displayName,
            shortDisplayName,
            ZonedDateTime.parse(startTime),
            ZonedDateTime.parse(endTime)
        )
    }
}