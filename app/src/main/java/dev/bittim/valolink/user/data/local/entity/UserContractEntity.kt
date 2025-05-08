/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserContractEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.05.25, 15:21
 */

package dev.bittim.valolink.user.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.user.domain.model.UserContract
import dev.bittim.valolink.user.domain.model.UserLevel
import java.time.OffsetDateTime

@Entity(
    tableName = "UserContracts", foreignKeys = [ForeignKey(
        entity = UserMetaEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["user"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(
        value = ["uuid"], unique = true
    ), Index(
        value = ["user", "contract"], unique = true
    )]
)
data class UserContractEntity(
    @PrimaryKey override val uuid: String,
    override val isSynced: Boolean,
    override val toDelete: Boolean,
    override val updatedAt: String,
    val user: String,
    val contract: String,
    val freeOnly: Boolean,
) : SyncedEntity {
    override fun getIdentifier(): String {
        return contract
    }

    override fun withIsSynced(isSynced: Boolean): SyncedEntity {
        return this.copy(isSynced = true)
    }

    fun toType(
        levels: List<UserLevel>,
    ): UserContract {
        return UserContract(
            uuid, user, contract, levels, freeOnly
        )
    }

    companion object {
        fun fromType(
            userContract: UserContract,
            isSynced: Boolean,
            toDelete: Boolean,
        ): UserContractEntity {
            return UserContractEntity(
                userContract.uuid,
                isSynced,
                toDelete,
                OffsetDateTime.now().toString(),
                userContract.user,
                userContract.contract,
                userContract.freeOnly
            )
        }
    }
}
