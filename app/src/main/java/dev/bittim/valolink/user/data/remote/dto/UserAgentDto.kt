/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       UserAgentDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.user.data.remote.dto

import dev.bittim.valolink.user.data.local.entity.UserAgentEntity
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
