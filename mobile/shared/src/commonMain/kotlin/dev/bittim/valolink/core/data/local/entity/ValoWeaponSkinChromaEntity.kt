/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoWeaponSkinChromaEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_weapon_skin_chromas",
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
data class ValoWeaponSkinChromaEntity(
    val uuid: Uuid,
    val skin: Uuid,
    val displayName: Map<String, String>,
    val displayIcon: String?,
    val fullRender: String,
    val swatch: String?,
    val streamedVideo: String?
)