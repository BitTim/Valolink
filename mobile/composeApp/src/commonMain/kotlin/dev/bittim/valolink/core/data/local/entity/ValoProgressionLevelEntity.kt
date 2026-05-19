/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoProgressionLevelEntity.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.05.26, 01:10
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_progression_levels",
    primaryKeys = ["progression", "levelIndex"],
    foreignKeys = [
        ForeignKey(
            entity = ValoProgressionEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["progression"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ValoProgressionLevelEntity(
    val progression: Uuid,
    val levelIndex: Int,
    val xp: Int,
    val isPurchasableVp: Boolean,
    val isPurchasableKc: Boolean,
    val vpCost: Int,
    val kcCost: Int,
    val isEpilogue: Boolean
)