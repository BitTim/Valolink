/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchParticipantWithMatchDto.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 17:09
 */

package dev.bittim.valolink.core.data.remote.dto

import dev.bittim.valolink.core.domain.model.MatchParticipant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class MatchParticipantWithMatchDto(
    @SerialName("user_id") val userId: Uuid,
    val activity: Uuid,
    val match: Uuid,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("updated_at") val updatedAt: Instant,
    @SerialName("visible_rr") val visibleRr: Int?,
    @SerialName("is_owner") val isOwner: Boolean,
    @SerialName("is_team_b") val isTeamB: Boolean,
    val matches: MatchDto?
) {
    fun toModel(): MatchParticipant =
        MatchParticipant(
            userId = userId,
            visibleRr = visibleRr,
            isOwner = isOwner,
            isTeamB = isTeamB
        )

}
