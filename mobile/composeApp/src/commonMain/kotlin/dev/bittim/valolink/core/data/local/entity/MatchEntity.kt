/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchEntity.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   18.05.26, 13:18
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import dev.bittim.valolink.core.domain.model.MatchEndReason
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(
    tableName = "matches",
    primaryKeys = ["id"],
    foreignKeys = [
        ForeignKey(
            entity = ValoMapEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["map"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ValoModeEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["mode"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MatchEntity(
    val id: Uuid,
    val createdAt: Instant,
    val updatedAt: Instant,
    val scoreA: Int,
    val scoreB: Int?,
    val endReason: MatchEndReason,
    val isRanked: Boolean,
    val map: Uuid,
    val mode: Uuid
)