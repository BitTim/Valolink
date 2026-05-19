/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoBuddyEntity.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   18.05.26, 14:53
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_buddies",
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
data class ValoBuddyEntity(
    val uuid: Uuid,
    val parent: Uuid,
    val theme: Uuid?,
    val displayName: Map<String, String>,
    val charmLevel: Int,
    val displayIcon: String,
    val hideIfNotOwned: Boolean
)