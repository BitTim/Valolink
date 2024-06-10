package dev.bittim.valolink.main.data.remote.user.dto

import dev.bittim.valolink.main.data.local.user.entity.UserDataEntity
import kotlinx.serialization.Serializable

@Serializable
data class UserDataDto(
    val uuid: String,
    override val updatedAt: String,
    val isPrivate: Boolean,
    val username: String,
    val agents: List<String>,
) : SyncedDto() {
    fun toEntity(): UserDataEntity {
        return UserDataEntity(
            uuid,
            true,
            updatedAt,
            isPrivate,
            username,
            agents
        )
    }

    companion object {
        fun fromEntity(userData: UserDataEntity): UserDataDto {
            return UserDataDto(
                userData.uuid,
                userData.updatedAt,
                userData.isPrivate,
                userData.username,
                userData.agents
            )
        }
    }
}
