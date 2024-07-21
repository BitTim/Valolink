package dev.bittim.valolink.main.data.local.user.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.main.domain.model.user.Progression
import java.time.OffsetDateTime

@Entity(
    tableName = "Progressions",
    indices = [Index(
        value = ["uuid"],
        unique = true
    ), Index(
        value = ["user", "contract"],
        unique = true
    )]
)
data class ProgressionEntity(
    @PrimaryKey val uuid: String,
    override val isSynced: Boolean,
    override val updatedAt: String,
    val user: String,
    val contract: String,
) : SyncedEntity() {
    fun toType(): Progression {
        return Progression(
            uuid,
            user,
            contract,
            emptyList()
        )
    }

    companion object {
        fun fromType(progression: Progression): ProgressionEntity {
            return ProgressionEntity(
                progression.uuid,
                false,
                OffsetDateTime.now().toString(),
                progression.user,
                progression.contract,
            )
        }
    }
}
