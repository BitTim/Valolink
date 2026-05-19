/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       PurchasedLevelEntity.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   18.05.26, 14:15
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(
    tableName = "purchased_levels",
    primaryKeys = ["userId", "progression", "level"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ValoProgressionLevelEntity::class,
            parentColumns = ["progression", "levelIndex"],
            childColumns = ["progression", "levelIndex"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PurchasedLevelEntity(
    val userId: Uuid,
    val progression: Uuid,
    val levelIndex: Int,
    val createdAt: Instant
)