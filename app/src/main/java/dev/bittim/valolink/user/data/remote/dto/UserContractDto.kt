/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserContractDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   25.04.25, 19:03
 */

package dev.bittim.valolink.user.data.remote.dto

import dev.bittim.valolink.user.data.local.entity.UserContractEntity
import kotlinx.serialization.Serializable

@Serializable
data class UserContractDto(
    override val uuid: String,
    override val updatedAt: String,
    val user: String,
    val contract: String,
    val freeOnly: Boolean,
) : SyncedDto<UserContractEntity>() {
    override fun getIdentifier(): String {
        return contract
    }

    override fun toEntity(isSynced: Boolean, toDelete: Boolean): UserContractEntity {
        return UserContractEntity(uuid, isSynced, toDelete, updatedAt, user, contract, freeOnly)
    }

    companion object {
        fun fromEntity(entity: UserContractEntity): UserContractDto {
            return UserContractDto(
                entity.uuid,
                entity.updatedAt,
                entity.user,
                entity.contract,
                entity.freeOnly
            )
        }
    }
}
