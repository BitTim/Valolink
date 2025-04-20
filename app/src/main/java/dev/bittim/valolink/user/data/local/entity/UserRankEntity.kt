/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserRankEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   20.04.25, 03:29
 */

package dev.bittim.valolink.user.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.user.domain.model.UserRank
import java.time.OffsetDateTime

@Entity(
    tableName = "UserRanks", foreignKeys = [ForeignKey(
        entity = UserDataEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["user"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(
        value = ["uuid"], unique = true
    )]
)
data class UserRankEntity(
    @PrimaryKey override val uuid: String,
    override val isSynced: Boolean,
    override val toDelete: Boolean,
    override val updatedAt: String,
    val user: String,
    val tier: Int,
    val rr: Int,
    val matchesPlayed: Int,
    val matchesNeeded: Int,
) : SyncedEntity {
    override fun getIdentifier(): String {
        return user
    }

    override fun withIsSynced(isSynced: Boolean): SyncedEntity {
        return this.copy(isSynced = true)
    }

    fun toType(): UserRank {
        return UserRank(
            uuid, user, tier, rr, matchesPlayed, matchesNeeded
        )
    }

    companion object {
        fun fromType(
            userRank: UserRank,
            isSynced: Boolean,
            toDelete: Boolean
        ): UserRankEntity {
            return UserRankEntity(
                userRank.uuid,
                isSynced,
                toDelete,
                OffsetDateTime.now().toString(),
                userRank.user,
                userRank.tier,
                userRank.rr,
                userRank.matchesPlayed,
                userRank.matchesNeeded
            )
        }
    }
}
