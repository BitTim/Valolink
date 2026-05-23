/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoSeasonEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_seasons",
    primaryKeys = ["uuid"]
)
data class ValoSeasonEntity(
    val uuid: Uuid,
    val displayName: Map<String, String>,
    val episodeDisplayName: Map<String, String>?,
    val title: Map<String, String>?,
    val startTime: Instant,
    val endTime: Instant
)