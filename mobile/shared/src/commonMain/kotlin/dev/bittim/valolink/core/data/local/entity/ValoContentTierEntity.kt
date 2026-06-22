/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoContentTierEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_content_tiers",
    primaryKeys = ["uuid"]
)
data class ValoContentTierEntity(
    val uuid: Uuid,
    val displayName: Map<String, String>,
    val developerName: String,
    val displayIcon: String,
    val juiceCost: Int,
    val juiceValue: Int,
    val highlightColor: String,
    val rank: Int
)