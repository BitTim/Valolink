package dev.bittim.valolink.main.data.remote.user.dto

import dev.bittim.valolink.main.data.local.user.entity.ProgressionLevelEntity
import kotlinx.serialization.Serializable

@Serializable
data class ProgressionLevelDto(
    val uuid: String,
    override val updatedAt: String,
    val progression: String,
    val level: String,
    val isPurchased: Boolean,
) : SyncedDto() {
    fun toEntity(): ProgressionLevelEntity {
        return ProgressionLevelEntity(
            uuid,
            true,
            updatedAt,
            progression,
            level,
            isPurchased
        )
    }

    companion object {
        fun fromEntity(entity: ProgressionLevelEntity): ProgressionLevelDto {
            return ProgressionLevelDto(
                entity.uuid,
                entity.updatedAt,
                entity.progression,
                entity.level,
                entity.isPurchased
            )
        }
    }
}
