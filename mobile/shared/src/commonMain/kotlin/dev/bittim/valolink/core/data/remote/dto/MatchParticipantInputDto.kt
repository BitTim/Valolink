/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchParticipantInputDto.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   25.08.26, 21:44
 */

package dev.bittim.valolink.core.data.remote.dto

import dev.bittim.valolink.core.domain.model.MatchParticipantDraft
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MatchParticipantInputDto(
    @SerialName("visible_rr") val visibleRr: Int?,
    @SerialName("is_owner") val isOwner: Boolean,
    @SerialName("is_team_b") val isTeamB: Boolean
) {
    companion object {
        fun fromModel(matchParticipantDraft: MatchParticipantDraft): MatchParticipantInputDto {
            return MatchParticipantInputDto(
                visibleRr = matchParticipantDraft.visibleRr,
                isOwner = matchParticipantDraft.isOwner,
                isTeamB = matchParticipantDraft.isTeamB
            )
        }

    }
}
