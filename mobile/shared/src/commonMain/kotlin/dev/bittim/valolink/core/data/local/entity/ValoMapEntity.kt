/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoMapEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import dev.bittim.valolink.core.data.local.embedded.ValoMapCallout
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_maps",
    primaryKeys = ["uuid"]
)
data class ValoMapEntity(
    val uuid: Uuid,
    val displayName: Map<String, String>,
    val tacticalDescription: Map<String, String>?,
    val coordinates: Map<String, String>?,
    val category: String,
    val listViewIcon: String,
    val listViewIconTall: String,
    val splash: String,
    val premierBackgroundImage: String?,
    val stylizedBackgroundImage: String?,
    val displayIcon: String?,
    val xMultiplier: Float,
    val xScalarToAdd: Float,
    val yMultiplier: Float,
    val yScalarToAdd: Float,
    val callouts: List<ValoMapCallout>?
)