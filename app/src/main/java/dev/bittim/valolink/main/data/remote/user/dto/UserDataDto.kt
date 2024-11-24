package dev.bittim.valolink.main.data.remote.user.dto

import dev.bittim.valolink.main.data.local.user.entity.UserDataEntity
import kotlinx.serialization.Serializable

@Serializable
data class UserDataDto(
	override val uuid: String,
	override val updatedAt: String,
	val isPrivate: Boolean,
	val username: String,
) : SyncedDto<UserDataEntity>() {
	override fun getIdentifier(): String {
		return uuid
	}

	override fun toEntity(isSynced: Boolean, toDelete: Boolean): UserDataEntity {
		return UserDataEntity(uuid, isSynced, toDelete, updatedAt, isPrivate, username)
	}

	companion object {
		fun fromEntity(userData: UserDataEntity): UserDataDto {
			return UserDataDto(
				userData.uuid, userData.updatedAt, userData.isPrivate, userData.username
			)
		}
	}
}
