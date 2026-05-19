/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoAgentRoleEntity.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   18.05.26, 14:43
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_agent_roles",
    primaryKeys = ["uuid"]
)
data class ValoAgentRoleEntity(
    val uuid: Uuid,
    val displayName: Map<String, String>,
    val description: Map<String, String>,
    val displayIcon: String
)