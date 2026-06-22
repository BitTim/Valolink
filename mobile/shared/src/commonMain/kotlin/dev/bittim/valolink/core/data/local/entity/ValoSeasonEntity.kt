/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoSeasonEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   22.06.26, 04:02
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import dev.bittim.valolink.core.data.util.localized
import dev.bittim.valolink.core.domain.model.ValoSeason
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_seasons",
    primaryKeys = ["uuid"]
)
data class ValoSeasonEntity(
    val uuid: Uuid,
    val displayName: Map<String, String>,
    val episodeDisplayName: Map<String, String>?,
    val title: Map<String, String>?,
    val startTime: Instant,
    val endTime: Instant
) {
    fun toModel(locale: String?): ValoSeason {
        return ValoSeason(
            uuid = uuid,
            displayName = displayName.localized(locale),
            episodeDisplayName = episodeDisplayName?.localized(locale),
            title = title?.localized(locale),
            startTime = startTime,
            endTime = endTime
        )
    }
}