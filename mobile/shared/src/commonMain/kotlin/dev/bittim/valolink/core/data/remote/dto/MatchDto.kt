/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchDto.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.08.26, 17:27
 */

package dev.bittim.valolink.core.data.remote.dto

import dev.bittim.valolink.core.domain.model.Match
import dev.bittim.valolink.core.domain.model.MatchEndReason
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Clock
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
    fun toModel(): Match {
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

    companion object {
        fun fromModel(match: Match): MatchDto {
            return MatchDto(
                id = match.id,
                createdAt = Clock.System.now(),
                updatedAt = Clock.System.now(),
                scoreA = match.scoreA,
                scoreB = match.scoreB,
                endReason = match.endReason,
                isRanked = match.isRanked,
                time = match.time,
                map = match.map,
                mode = match.mode
            )
        }

    }
}
