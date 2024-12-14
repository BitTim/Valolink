/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       UserLevelEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.user.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.user.domain.model.UserLevel
import java.time.OffsetDateTime

@Entity(
    tableName = "UserLevels", foreignKeys = [ForeignKey(
        entity = UserContractEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["userContract"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(
        value = ["uuid"], unique = true
    ), Index(
        value = ["userContract", "level"], unique = true
    )]
)
data class UserLevelEntity(
    @PrimaryKey override val uuid: String,
    override val isSynced: Boolean,
    override val toDelete: Boolean,
    override val updatedAt: String,
    val userContract: String,
    val level: String,
    val isPurchased: Boolean,
) : SyncedEntity {
    override fun getIdentifier(): String {
        return level
    }

    override fun withIsSynced(isSynced: Boolean): SyncedEntity {
        return this.copy(isSynced = true)
    }

    fun toType(): UserLevel {
        return UserLevel(
            uuid, userContract, level, isPurchased
        )
    }

    companion object {
        fun fromType(userLevel: UserLevel, isSynced: Boolean, toDelete: Boolean): UserLevelEntity {
            return UserLevelEntity(
                userLevel.uuid,
                isSynced,
                toDelete,
                OffsetDateTime.now().toString(),
                userLevel.userContract,
                userLevel.level,
                userLevel.isPurchased
            )
        }
    }
}
