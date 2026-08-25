/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchInputDto.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   25.08.26, 21:19
 */

package dev.bittim.valolink.core.data.remote.dto

import dev.bittim.valolink.core.domain.model.MatchDraft
import dev.bittim.valolink.core.domain.model.MatchEndReason
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class MatchInputDto(
    @SerialName("score_a") val scoreA: Int,
    @SerialName("score_b") val scoreB: Int?,
    @SerialName("end_reason") val endReason: MatchEndReason,
    @SerialName("is_ranked") val isRanked: Boolean,
    val time: Instant,
    val map: Uuid,
    val mode: Uuid
) {
    companion object {
        /**
         * Creates match input data from a match draft.
         *
         * @param matchDraft The match draft to convert.
         * @return The corresponding match input data.
         */
        fun fromModel(matchDraft: MatchDraft): MatchInputDto {
            return MatchInputDto(
                scoreA = matchDraft.scoreA,
                scoreB = matchDraft.scoreB,
                endReason = matchDraft.endReason,
                isRanked = matchDraft.isRanked,
                time = matchDraft.time,
                map = matchDraft.map,
                mode = matchDraft.mode
            )
        }

    }
}
