/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchParticipantDto.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.08.26, 17:29
 */

package dev.bittim.valolink.core.data.remote.dto

import dev.bittim.valolink.core.domain.model.MatchParticipant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class MatchParticipantDto(
    @SerialName("user_id") val userId: Uuid,
    val activity: Uuid,
    val match: Uuid,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("updated_at") val updatedAt: Instant,
    @SerialName("visible_rr") val visibleRr: Int?,
    @SerialName("is_owner") val isOwner: Boolean,
    @SerialName("is_team_b") val isTeamB: Boolean
) {
    fun toModel(): MatchParticipant {
        return MatchParticipant(
            userId = userId,
            activity = activity,
            match = match,
            visibleRr = visibleRr,
            isOwner = isOwner,
            isTeamB = isTeamB
        )
    }

    companion object {
        fun fromModel(matchParticipant: MatchParticipant): MatchParticipantDto {
            return MatchParticipantDto(
                userId = matchParticipant.userId,
                activity = matchParticipant.activity,
                match = matchParticipant.match,
                createdAt = Clock.System.now(),
                updatedAt = Clock.System.now(),
                visibleRr = matchParticipant.visibleRr,
                isOwner = matchParticipant.isOwner,
                isTeamB = matchParticipant.isTeamB
            )
        }

    }
}
