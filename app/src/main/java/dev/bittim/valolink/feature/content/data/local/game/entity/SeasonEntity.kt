package dev.bittim.valolink.feature.content.data.local.game.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.domain.model.Season
import java.time.ZonedDateTime

@Entity
data class SeasonEntity(
    @PrimaryKey
    val uuid: String,
    override val version: String,
    val displayName: String,
    val type: String?,
    val startTime: String,
    val endTime: String,
    val parentUuid: String?
) : GameEntity<Season>() {
    override fun toType(): Season {
        return Season(
            uuid = uuid,
            name = displayName,
            startTime = ZonedDateTime.parse(startTime),
            endTime = ZonedDateTime.parse(endTime)
        )
    }
}
