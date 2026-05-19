/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoCurrencyEntity.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   18.05.26, 15:21
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_currencies",
    primaryKeys = ["uuid"]
)
data class ValoCurrencyEntity(
    val uuid: Uuid,
    val displayName: Map<String, String>,
    val displayNameSingular: Map<String, String>,
    val displayIcon: String,
    val largeIcon: String,
    val rewardPreviewIcon: String
)