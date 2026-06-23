/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoCompetitiveSeasonDto.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.06.26, 02:00
 */

package dev.bittim.valolink.core.data.remote.dto

import dev.bittim.valolink.core.data.local.entity.ValoCompetitiveSeasonEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class ValoCompetitiveSeasonDto(
    val uuid: Uuid,
    val season: Uuid,
    @SerialName("rank_table") val rankTable: Uuid,
    @SerialName("start_time") val startTime: Instant,
    @SerialName("end_time") val endTime: Instant,
) {
    /**
     * Converts this DTO to a domain entity.
     *
     * @return The corresponding [ValoCompetitiveSeasonEntity].
     */
    fun toEntity(): ValoCompetitiveSeasonEntity {
        return ValoCompetitiveSeasonEntity(
            uuid = uuid,
            season = season,
            rankTable = rankTable,
            startTime = startTime,
            endTime = endTime,
        )
    }

}
