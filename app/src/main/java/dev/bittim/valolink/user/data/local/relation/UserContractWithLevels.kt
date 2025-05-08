/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserContractWithLevels.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   04.05.25, 13:35
 */

package dev.bittim.valolink.user.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.user.data.local.entity.SyncedEntity
import dev.bittim.valolink.user.data.local.entity.UserContractEntity
import dev.bittim.valolink.user.data.local.entity.UserLevelEntity
import dev.bittim.valolink.user.domain.model.UserContract

data class UserContractWithLevels(
    @Embedded val contract: UserContractEntity,
    @Relation(
        parentColumn = "uuid", entityColumn = "userContract"
    ) val levels: List<UserLevelEntity>,
) : SyncedEntity {
    override val uuid: String
        get(): String = contract.uuid
    override val isSynced: Boolean
        get() = contract.isSynced && levels.all { it.isSynced }
    override val toDelete: Boolean
        get() = contract.toDelete
    override val updatedAt: String
        get() = contract.updatedAt

    override fun getIdentifier(): String {
        return contract.getIdentifier()
    }

    override fun withIsSynced(isSynced: Boolean): SyncedEntity {
        return contract.withIsSynced(isSynced)
    }

    fun toType(): UserContract {
        return contract.toType(levels.map { it.toType() })
    }
}
