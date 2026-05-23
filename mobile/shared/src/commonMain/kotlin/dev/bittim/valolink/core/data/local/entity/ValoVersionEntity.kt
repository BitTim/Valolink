/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoVersionEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import kotlin.time.Instant

@Entity(
    tableName = "valo_version",
    primaryKeys = ["id"]
)
data class ValoVersionEntity(
    val id: Int,
    val branch: String,
    val buildVersion: String,
    val manifestId: String,
    val riotClientBuild: String,
    val riotClientVersion: String,
    val engineVersion: String,
    val version: String,
    val buildDate: Instant
)