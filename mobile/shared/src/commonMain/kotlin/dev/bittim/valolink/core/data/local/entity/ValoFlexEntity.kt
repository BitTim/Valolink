/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoFlexEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_flex",
    primaryKeys = ["uuid"]
)
data class ValoFlexEntity(
    val uuid: Uuid,
    val displayName: Map<String, String>,
    val displayNameAllCaps: Map<String, String>,
    val displayIcon: String
)