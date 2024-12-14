/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       UserDataDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.user.data.remote.dto

import dev.bittim.valolink.user.data.local.entity.UserDataEntity
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
