/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchDto.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 19:22
 */

package dev.bittim.valolink.core.data.remote.dto

import dev.bittim.valolink.core.domain.model.Match
import dev.bittim.valolink.core.domain.model.MatchEndReason
import dev.bittim.valolink.core.domain.model.SimpleValoMap
import dev.bittim.valolink.core.domain.model.ValoMode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class MatchDto(
    val id: Uuid,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("updated_at") val updatedAt: Instant,
    @SerialName("score_a") val scoreA: Int,
    @SerialName("score_b") val scoreB: Int?,
    @SerialName("end_reason") val endReason: MatchEndReason,
    @SerialName("is_ranked") val isRanked: Boolean,
    val time: Instant,
    val map: Uuid,
    val mode: Uuid
) {
    /**
     * Converts this data-transfer object into a match domain model.
     *
     * @return The match represented by this data-transfer object.
     */
    fun toModel(map: SimpleValoMap, mode: ValoMode): Match {
        return Match(
            id = id,
            scoreA = scoreA,
            scoreB = scoreB,
            endReason = endReason,
            isRanked = isRanked,
            time = time,
            map = map,
            mode = mode
        )
    }
}
