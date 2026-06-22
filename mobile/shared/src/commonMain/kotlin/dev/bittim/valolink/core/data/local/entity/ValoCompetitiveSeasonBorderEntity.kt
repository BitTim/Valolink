/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoCompetitiveSeasonBorderEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_competitive_season_borders",
    primaryKeys = ["uuid"],
    foreignKeys = [
        ForeignKey(
            entity = ValoCompetitiveSeasonEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["competitiveSeason"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ValoCompetitiveSeasonBorderEntity(
    val uuid: Uuid,
    val competitiveSeason: Uuid,
    val displayIcon: String,
    val smallIcon: String?,
    val level: Int,
    val winsRequired: Int
)