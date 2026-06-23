/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoCompetitiveSeasonEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.06.26, 02:28
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import dev.bittim.valolink.core.domain.model.ValoCompetitiveSeason
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_competitive_seasons",
    primaryKeys = ["uuid"],
    foreignKeys = [
        ForeignKey(
            entity = ValoSeasonEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["season"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ValoRankTableEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["rankTable"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ValoCompetitiveSeasonEntity(
    val uuid: Uuid,
    val season: Uuid,
    val rankTable: Uuid,
    val startTime: Instant,
    val endTime: Instant
) {
    /**
     * Converts this competitive season entity to its domain model representation.
     *
     * @return The domain model instance with all properties mapped from the entity.
     */
    fun toModel(): ValoCompetitiveSeason {
        return ValoCompetitiveSeason(
            uuid = uuid,
            season = season,
            rankTable = rankTable,
            startTime = startTime,
            endTime = endTime
        )
    }
}