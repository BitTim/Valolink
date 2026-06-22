/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoProgressionLevelRewardEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_progression_level_rewards",
    primaryKeys = ["progression", "levelIndex", "sortOrder"],
    foreignKeys = [
        ForeignKey(
            entity = ValoProgressionLevelEntity::class,
            parentColumns = ["progression", "levelIndex"],
            childColumns = ["progression", "levelIndex"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ValoProgressionLevelRewardEntity(
    val progression: Uuid,
    val levelIndex: Int,
    val sortOrder: Int,
    val relationType: String,
    val relationUuid: Uuid,
    val amount: Int,
    val isFree: Boolean
)