/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoTitleEntity.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.05.26, 01:19
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_titles",
    primaryKeys = ["uuid"]
)
data class ValoTitleEntity(
    val uuid: Uuid,
    val displayName: Map<String, String>?,
    val titleText: Map<String, String>?,
    val hideIfNotOwned: Boolean
)