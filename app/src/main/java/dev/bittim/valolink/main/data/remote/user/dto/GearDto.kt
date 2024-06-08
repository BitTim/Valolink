package dev.bittim.valolink.main.data.remote.user.dto

import dev.bittim.valolink.main.data.local.user.entity.GearEntity
import kotlinx.serialization.Serializable

@Serializable
data class GearDto(
    val uuid: String,
    override val updatedAt: String,
    val user: String,
    val contract: String,
    val progress: Int,
) : SyncedDto() {
    fun toEntity(): GearEntity {
        return GearEntity(
            uuid,
            true,
            updatedAt,
            user,
            contract,
            progress,
        )
    }

    companion object {
        fun fromEntity(entity: GearEntity): GearDto {
            return GearDto(
                entity.uuid,
                entity.updatedAt,
                entity.user,
                entity.contract,
                entity.progress,
            )
        }
    }
}
