package dev.bittim.valolink.main.data.local.user.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.main.domain.model.user.ProgressionLevel
import java.time.OffsetDateTime

@Entity(
    tableName = "ProgressionLevels",
    indices = [Index(
        value = ["uuid"],
        unique = true
    ), Index(
        value = ["user", "progression"],
        unique = true
    )]
)
data class ProgressionLevelEntity(
    @PrimaryKey val uuid: String,
    override val isSynced: Boolean,
    override val updatedAt: String,
    val progression: String,
    val level: String,
    val isPurchased: Boolean,
) : SyncedEntity() {
    fun toType(): ProgressionLevel {
        return ProgressionLevel(
            uuid,
            progression,
            level,
            isPurchased
        )
    }

    companion object {
        fun fromType(progressionLevel: ProgressionLevel): ProgressionLevelEntity {
            return ProgressionLevelEntity(
                progressionLevel.uuid,
                false,
                OffsetDateTime.now().toString(),
                progressionLevel.progression,
                progressionLevel.level,
                progressionLevel.isPurchased
            )
        }
    }
}
