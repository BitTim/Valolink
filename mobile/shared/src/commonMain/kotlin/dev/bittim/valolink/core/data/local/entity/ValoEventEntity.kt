/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoEventEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_events",
    primaryKeys = ["uuid"]
)
data class ValoEventEntity(
    val uuid: Uuid,
    val displayName: Map<String, String>?,
    val shortDisplayName: Map<String, String>?,
    val startTime: Instant,
    val endTime: Instant
)