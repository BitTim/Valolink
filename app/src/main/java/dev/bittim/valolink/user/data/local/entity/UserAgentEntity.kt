/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserAgentEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   04.05.25, 10:54
 */

package dev.bittim.valolink.user.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.user.domain.model.UserAgent
import java.time.OffsetDateTime

@Entity(
    tableName = "UserAgents", foreignKeys = [ForeignKey(
        entity = UserMetaEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["user"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(
        value = ["uuid"], unique = true
    ), Index(
        value = ["user", "agent"], unique = true
    )]
)
data class UserAgentEntity(
    @PrimaryKey override val uuid: String,
    override val isSynced: Boolean,
    override val toDelete: Boolean,
    override val updatedAt: String,
    val user: String,
    val agent: String,
) : SyncedEntity {
    override fun getIdentifier(): String {
        return agent
    }

    override fun withIsSynced(isSynced: Boolean): SyncedEntity {
        return this.copy(isSynced = true)
    }

    fun toType(): UserAgent {
        return UserAgent(
            uuid, user, agent
        )
    }

    companion object {
        fun fromType(
            userAgent: UserAgent,
            isSynced: Boolean,
            toDelete: Boolean
        ): UserAgentEntity {
            return UserAgentEntity(
                userAgent.uuid,
                isSynced,
                toDelete,
                OffsetDateTime.now().toString(),
                userAgent.user,
                userAgent.agent
            )
        }
    }
}
