/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoCardEntity.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   18.05.26, 14:57
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_cards",
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
data class ValoCardEntity(
    val uuid: Uuid,
    val theme: Uuid?,
    val displayName: Map<String, String>,
    val displayIcon: String,
    val largeArt: String?,
    val wideArt: String,
    val smallArt: String,
    val hideIfNotOwned: Boolean
)