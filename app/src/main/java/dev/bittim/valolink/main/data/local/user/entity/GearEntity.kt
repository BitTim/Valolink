package dev.bittim.valolink.main.data.local.user.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.main.domain.model.Gear
import java.time.OffsetDateTime

@Entity(
    tableName = "Gears",
    indices = [Index(
        value = ["uuid"],
        unique = true
    ), Index(
        value = ["user", "contract"],
        unique = true
    )]
)
data class GearEntity(
    @PrimaryKey val uuid: String,
    override val isSynced: Boolean,
    override val updatedAt: String,
    val user: String,
    val contract: String,
    val progress: Int,
) : SyncedEntity() {
    fun toType(): Gear {
        return Gear(
            uuid,
            user,
            contract,
            progress
        )
    }

    companion object {
        fun fromType(gear: Gear): GearEntity {
            return GearEntity(
                gear.uuid,
                false,
                OffsetDateTime.now().toString(),
                gear.user,
                gear.contract,
                gear.progress
            )
        }
    }
}
