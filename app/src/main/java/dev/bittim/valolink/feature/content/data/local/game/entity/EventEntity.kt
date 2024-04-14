package dev.bittim.valolink.feature.content.data.local.game.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.domain.model.Event

@Entity
data class EventEntity(
    @PrimaryKey
    val uuid: String,
    override val version: String,
    val displayName: String,
    val shortDisplayName: String,
    val startTime: String,
    val endTime: String,
    val assetPath: String
) : GameEntity<Event>() {
    fun toType(): Event {
        return Event(
            uuid = uuid
        )
    }
}