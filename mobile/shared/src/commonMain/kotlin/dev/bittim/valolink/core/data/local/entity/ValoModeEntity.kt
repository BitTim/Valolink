/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoModeEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_modes",
    primaryKeys = ["uuid"]
)
data class ValoModeEntity(
    val uuid: Uuid,
    val displayName: Map<String, String>,
    val description: Map<String, String>?,
    val duration: Map<String, String>?,
    val category: String,
    val displayIcon: String?,
    val listViewIconTall: String?,
    val roundsPerHalf: Int,
    val canBeRanked: Boolean
)