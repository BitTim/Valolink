package dev.bittim.valolink.main.data.remote.user.dto

import dev.bittim.valolink.main.data.local.user.entity.UserAgentEntity
import kotlinx.serialization.Serializable

@Serializable
data class UserAgentDto(
	override val uuid: String,
	override val updatedAt: String,
	val user: String,
	val agent: String,
) : SyncedDto<UserAgentEntity>() {
	override fun getIdentifier(): String {
		return agent
	}

	override fun toEntity(isSynced: Boolean, toDelete: Boolean): UserAgentEntity {
		return UserAgentEntity(uuid, isSynced, toDelete, updatedAt, user, agent)
	}

	companion object {
		fun fromEntity(entity: UserAgentEntity): UserAgentDto {
			return UserAgentDto(
				entity.uuid,
				entity.updatedAt,
				entity.user,
				entity.agent,
			)
		}
	}
}
