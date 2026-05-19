/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoSprayEntity.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.05.26, 01:17
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_sprays",
    primaryKeys = ["uuid"],
    foreignKeys = [
        ForeignKey(
            entity = ValoThemeEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["theme"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ValoSprayEntity(
    val uuid: Uuid,
    val theme: Uuid?,
    val displayName: Map<String, String>,
    val displayIcon: String,
    val fullIcon: String?,
    val fullTransparentIcon: String?,
    val animationPng: String?,
    val animationGif: String?,
    val hideIfNotOwned: Boolean
)