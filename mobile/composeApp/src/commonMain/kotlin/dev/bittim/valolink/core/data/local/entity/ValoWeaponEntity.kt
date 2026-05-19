/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoWeaponEntity.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.05.26, 01:36
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_weapons",
    primaryKeys = ["uuid"]
)
data class ValoWeaponEntity(
    val uuid: Uuid,
    val displayName: Map<String, String>,
    val category: String,
    val defaultSkin: Uuid,
    val displayIcon: String,
    val killStreamIcon: String
)