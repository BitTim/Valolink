/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoAgentAbilityEntity.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   18.05.26, 14:40
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_agent_abilities",
    primaryKeys = ["agent", "slot"],
    foreignKeys = [
        ForeignKey(
            entity = ValoAgentEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["agent"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ValoAgentAbilityEntity(
    val agent: Uuid,
    val slot: String,
    val displayName: Map<String, String>,
    val description: Map<String, String>,
    val displayIcon: String?
)