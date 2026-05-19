/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoModeEntity.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.05.26, 01:05
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