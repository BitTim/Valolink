/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AgentEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import dev.bittim.valolink.core.domain.model.AgentOwnedState
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(
    tableName = "agents",
    primaryKeys = ["userId", "agent"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ValoAgentEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["agent"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AgentEntity(
    val userId: Uuid,
    val agent: Uuid,
    val createdAt: Instant,
    val updatedAt: Instant,
    val ownedState: AgentOwnedState,
    val startTime: Instant?,
    val endTime: Instant?,
    val xpOffset: Int,
    val totalXp: Int
)
