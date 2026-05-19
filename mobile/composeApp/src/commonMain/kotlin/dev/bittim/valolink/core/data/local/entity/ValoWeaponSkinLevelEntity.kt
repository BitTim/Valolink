/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoWeaponSkinLevelEntity.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.05.26, 01:52
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_weapon_skin_levels",
    primaryKeys = ["uuid"],
    foreignKeys = [
        ForeignKey(
            entity = ValoWeaponSkinEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["skin"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ValoWeaponSkinLevelEntity(
    val uuid: Uuid,
    val skin: Uuid,
    val displayName: Map<String, String>,
    val displayIcon: String?,
    val streamedVideo: String?
)