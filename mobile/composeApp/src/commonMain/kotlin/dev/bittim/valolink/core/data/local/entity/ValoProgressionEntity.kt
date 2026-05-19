/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoProgressionEntity.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.05.26, 01:11
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_progressions",
    primaryKeys = ["uuid"]
)
data class ValoProgressionEntity(
    val uuid: Uuid,
    val displayName: Map<String, String>,
    val displayIcon: String?,
    val relationType: String?,
    val relationUuid: Uuid?,
    val premiumVpCost: Int
)