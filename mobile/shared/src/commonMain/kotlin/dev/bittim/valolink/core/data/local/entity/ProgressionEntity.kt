/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ProgressionEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(
    tableName = "progressions",
    primaryKeys = ["userId", "progression"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ValoProgressionEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["progression"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProgressionEntity(
    val userId: Uuid,
    val progression: Uuid,
    val createdAt: Instant,
    val updatedAt: Instant,
    val freeOnly: Boolean,
    val trackXp: Boolean,
    val startTime: Instant?,
    val endTime: Instant?,
    val xpOffset: Int,
    val totalXp: Int
)