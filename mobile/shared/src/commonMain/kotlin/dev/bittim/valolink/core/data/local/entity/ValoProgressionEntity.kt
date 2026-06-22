/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoProgressionEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
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