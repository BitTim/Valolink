/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoRankEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_ranks",
    primaryKeys = ["rankTable", "tier"],
    foreignKeys = [
        ForeignKey(
            entity = ValoRankTableEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["rankTable"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ValoRankEntity(
    val rankTable: Uuid,
    val tier: Int,
    val tierName: Map<String, String>,
    val smallIcon: String?,
    val largeIcon: String?,
    val rankTriangleDownIcon: String?,
    val rankTriangleUpIcon: String?,
    val color: String,
    val backgroundColor: String,
    val division: String,
    val divisionName: Map<String, String>
)