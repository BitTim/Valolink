/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserRankDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   23.04.25, 00:09
 */

package dev.bittim.valolink.user.data.remote.dto

import dev.bittim.valolink.user.data.local.entity.UserRankEntity
import kotlinx.serialization.Serializable

@Serializable
data class UserRankDto(
    override val uuid: String,
    override val updatedAt: String,
    val tier: Int,
    val rr: Int,
    val matchesPlayed: Int,
    val matchesNeeded: Int,
) : SyncedDto<UserRankEntity>() {
    override fun getIdentifier(): String {
        return uuid
    }

    override fun toEntity(isSynced: Boolean, toDelete: Boolean): UserRankEntity {
        return UserRankEntity(
            uuid,
            isSynced,
            toDelete,
            updatedAt,
            tier,
            rr,
            matchesPlayed,
            matchesNeeded
        )
    }

    companion object {
        fun fromEntity(entity: UserRankEntity): UserRankDto {
            return UserRankDto(
                entity.uuid,
                entity.updatedAt,
                entity.tier,
                entity.rr,
                entity.matchesPlayed,
                entity.matchesNeeded
            )
        }
    }
}
