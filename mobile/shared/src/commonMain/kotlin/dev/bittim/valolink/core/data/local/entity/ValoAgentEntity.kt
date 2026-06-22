/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoAgentEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_agents",
    primaryKeys = ["uuid"],
    foreignKeys = [
        ForeignKey(
            entity = ValoAgentRoleEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["role"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ValoAgentEntity(
    val uuid: Uuid,
    val displayName: Map<String, String>,
    val description: Map<String, String>,
    val developerName: String,
    val releaseDate: Instant,
    val displayIcon: String,
    val displayIconSmall: String,
    val bustPortrait: String,
    val fullPortrait: String,
    val fullPortraitV2: String,
    val killfeedPortrait: String,
    val minimapPortrait: String,
    val homeScreenPromoTileImage: String?,
    val background: String,
    val backgroundGradientColors: List<String>,
    val isFullPortraitRightFacing: Boolean,
    val isBaseContent: Boolean,
    val role: Uuid
)