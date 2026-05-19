/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ProgressionEntity.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   18.05.26, 13:35
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
            parentColumns = ["id"],
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