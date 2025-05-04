/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserMetaEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   04.05.25, 10:54
 */

package dev.bittim.valolink.user.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.user.domain.model.UserMeta
import dev.bittim.valolink.user.domain.model.UserRank
import java.time.OffsetDateTime

@Entity(
    tableName = "Users", indices = [Index(
        value = ["uuid"], unique = true
    )]
)
data class UserMetaEntity(
    @PrimaryKey override val uuid: String,
    override val isSynced: Boolean,
    override val toDelete: Boolean,
    override val updatedAt: String,
    val isPrivate: Boolean,
    val username: String,
    val onboardingStep: Int,
    val avatar: String?,
) : SyncedEntity {
    override fun getIdentifier(): String {
        return uuid
    }

    override fun withIsSynced(isSynced: Boolean): SyncedEntity {
        return this.copy(isSynced = true)
    }

    fun toType(
        rank: UserRank?
    ): UserMeta {
        return UserMeta(
            uuid,
            isPrivate,
            username,
            onboardingStep,
            avatar,
            rank
        )
    }

    companion object {
        fun fromType(
            userMeta: UserMeta,
            isSynced: Boolean,
            toDelete: Boolean
        ): UserMetaEntity {
            return UserMetaEntity(
                userMeta.uuid,
                isSynced,
                toDelete,
                OffsetDateTime.now().toString(),
                userMeta.isPrivate,
                userMeta.username,
                userMeta.onboardingStep,
                userMeta.avatar
            )
        }
    }
}
