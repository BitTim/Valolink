/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoWeaponSkinEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_weapon_skins",
    primaryKeys = ["uuid"],
    foreignKeys = [
        ForeignKey(
            entity = ValoWeaponEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["weapon"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ValoThemeEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["theme"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ValoContentTierEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["contentTier"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ValoWeaponSkinEntity(
    val uuid: Uuid,
    val weapon: Uuid,
    val displayName: Map<String, String>,
    val theme: Uuid,
    val contentTier: Uuid?,
    val displayIcon: String?,
    val wallpaper: String?
)