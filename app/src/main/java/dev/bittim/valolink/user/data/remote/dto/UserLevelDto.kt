/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       UserLevelDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.user.data.remote.dto

import dev.bittim.valolink.user.data.local.entity.UserLevelEntity
import kotlinx.serialization.Serializable

@Serializable
data class UserLevelDto(
    override val uuid: String,
    override val updatedAt: String,
    val userContract: String,
    val level: String,
    val isPurchased: Boolean,
) : SyncedDto<UserLevelEntity>() {
    override fun getIdentifier(): String {
        return level
    }

    override fun toEntity(isSynced: Boolean, toDelete: Boolean): UserLevelEntity {
        return UserLevelEntity(
            uuid, isSynced, toDelete, updatedAt, userContract, level, isPurchased
        )
    }

    companion object {
        fun fromEntity(entity: UserLevelEntity): UserLevelDto {
            return UserLevelDto(
                entity.uuid, entity.updatedAt, entity.userContract, entity.level, entity.isPurchased
            )
        }
    }
}
