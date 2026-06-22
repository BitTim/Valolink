/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoSeasonDto.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   22.06.26, 05:13
 */

package dev.bittim.valolink.core.data.remote.dto

import dev.bittim.valolink.core.data.local.entity.ValoSeasonEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class ValoSeasonDto(
    val uuid: Uuid,
    @SerialName("display_name") val displayName: Map<String, String>,
    @SerialName("episode_display_name") val episodeDisplayName: Map<String, String>?,
    val title: Map<String, String>?,
    @SerialName("start_time") val startTime: Instant,
    @SerialName("end_time") val endTime: Instant
) {
    fun toEntity(): ValoSeasonEntity {
        return ValoSeasonEntity(
            uuid = uuid,
            displayName = displayName,
            episodeDisplayName = episodeDisplayName,
            title = title,
            startTime = startTime,
            endTime = endTime
        )
    }

}
