package dev.bittim.valolink.feature.content.data.local.game.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.domain.model.Season
import java.time.ZonedDateTime

@Entity(
    tableName = "Seasons", indices = [Index(
        value = ["uuid"], unique = true
    )]
)
data class SeasonEntity(
    @PrimaryKey
    val uuid: String,
    override val version: String,
    val displayName: String,
    val type: String?,
    val startTime: String,
    val endTime: String,
    val parentUuid: String?
) : GameEntity() {
    fun toType(): Season {
        return Season(
            uuid, displayName, ZonedDateTime.parse(startTime), ZonedDateTime.parse(endTime)
        )
    }
}
