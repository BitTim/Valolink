/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       UserDataEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.user.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.user.domain.model.UserAgent
import dev.bittim.valolink.user.domain.model.UserContract
import dev.bittim.valolink.user.domain.model.UserData
import java.time.OffsetDateTime

@Entity(
    tableName = "Users", indices = [Index(
        value = ["uuid"], unique = true
    )]
)
data class UserDataEntity(
    @PrimaryKey override val uuid: String,
    override val isSynced: Boolean,
    override val toDelete: Boolean,
    override val updatedAt: String,
    val isPrivate: Boolean,
    val username: String,
) : SyncedEntity {
    override fun getIdentifier(): String {
        return uuid
    }

    override fun withIsSynced(isSynced: Boolean): SyncedEntity {
        return this.copy(isSynced = true)
    }

    fun toType(
        agents: List<UserAgent>,
        contracts: List<UserContract>,
    ): UserData {
        return UserData(
            uuid, isPrivate, username, agents, contracts
        )
    }

    companion object {
        fun fromType(userData: UserData, isSynced: Boolean, toDelete: Boolean): UserDataEntity {
            return UserDataEntity(
                userData.uuid,
                isSynced,
                toDelete,
                OffsetDateTime.now().toString(),
                userData.isPrivate,
                userData.username
            )
        }
    }
}
