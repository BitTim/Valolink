/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoCompetitiveSeasonEntity.kt
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
    tableName = "valo_competitive_seasons",
    primaryKeys = ["uuid"],
    foreignKeys = [
        ForeignKey(
            entity = ValoSeasonEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["season"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ValoRankTableEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["rankTable"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ValoCompetitiveSeasonEntity(
    val uuid: Uuid,
    val season: Uuid,
    val rankTable: Uuid,
    val startTime: Instant,
    val endTime: Instant
)