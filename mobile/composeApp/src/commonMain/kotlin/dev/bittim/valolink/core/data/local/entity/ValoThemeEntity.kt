/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoThemeEntity.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   18.05.26, 14:55
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_themes",
    primaryKeys = ["uuid"]
)
data class ValoThemeEntity(
    val uuid: Uuid,
    val displayName: Map<String, String>,
    val displayIcon: String?,
    val storeFeaturedImage: String?
)