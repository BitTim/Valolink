package dev.bittim.valolink.main.data.remote.user.dto

import dev.bittim.valolink.main.data.local.user.entity.ProgressionEntity
import kotlinx.serialization.Serializable

@Serializable
data class ProgressionDto(
    val uuid: String,
    override val updatedAt: String,
    val user: String,
    val contract: String,
) : SyncedDto() {
    fun toEntity(): ProgressionEntity {
        return ProgressionEntity(
            uuid,
            true,
            updatedAt,
            user,
            contract,
        )
    }

    companion object {
        fun fromEntity(entity: ProgressionEntity): ProgressionDto {
            return ProgressionDto(
                entity.uuid,
                entity.updatedAt,
                entity.user,
                entity.contract,
            )
        }
    }
}
