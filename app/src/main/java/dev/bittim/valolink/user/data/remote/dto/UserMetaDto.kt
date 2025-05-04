/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserMetaDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   04.05.25, 10:54
 */

package dev.bittim.valolink.user.data.remote.dto

import dev.bittim.valolink.user.data.local.entity.UserMetaEntity
import kotlinx.serialization.Serializable

@Serializable
data class UserMetaDto(
    override val uuid: String,
    override val updatedAt: String,
    val isPrivate: Boolean,
    val username: String,
    val onboardingStep: Int,
    val avatar: String?,
) : SyncedDto<UserMetaEntity>() {
    override fun getIdentifier(): String {
        return uuid
    }

    override fun toEntity(isSynced: Boolean, toDelete: Boolean): UserMetaEntity {
        return UserMetaEntity(
            uuid,
            isSynced,
            toDelete,
            updatedAt,
            isPrivate,
            username,
            onboardingStep,
            avatar
        )
    }

    companion object {
        fun fromEntity(userData: UserMetaEntity): UserMetaDto {
            return UserMetaDto(
                userData.uuid,
                userData.updatedAt,
                userData.isPrivate,
                userData.username,
                userData.onboardingStep,
                userData.avatar
            )
        }
    }
}
