/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import dev.bittim.valolink.core.domain.model.ActivityType
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(
    tableName = "activities",
    primaryKeys = ["id", "userId"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ActivityEntity(
    val id: Uuid,
    val userId: Uuid,
    val createdAt: Instant,
    val updatedAt: Instant,
    val time: Instant,
    val type: ActivityType,
    val xp: Int,
    val rr: Int?
)
