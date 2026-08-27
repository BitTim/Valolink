/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityWithMatchDto.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 19:10
 */

package dev.bittim.valolink.core.data.remote.dto

import dev.bittim.valolink.core.domain.model.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class ActivityWithMatchDto(
    val id: Uuid,
    @SerialName("user_id") val userId: Uuid,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("updated_at") val updatedAt: Instant,
    val time: Instant,
    val type: ActivityType,
    val xp: Int,
    val rr: Int?,
    val mode: Uuid?,
    @SerialName("match_participants") val matchParticipants: MatchParticipantWithMatchDto?
) {
    fun toModel(matchParticipant: MatchParticipant?, match: Match?, mode: ValoMode?): Activity {
        return when(type) {
            ActivityType.MATCH -> Activity.MatchActivity(
                id = id,
                userId = userId,
                time = time,
                xp = xp,
                rr = rr,
                matchParticipant = matchParticipant ?: throw IllegalArgumentException("Match participant not found"),
                match = match ?: throw IllegalArgumentException("Match not found")
            )
            ActivityType.RR_REFUND -> Activity.RrRefundActivity(
                id = id,
                userId = userId,
                time = time,
                rr = rr ?: throw IllegalArgumentException("RR not found"),
                mode = mode ?: throw IllegalArgumentException("Mode not found")
            )
            ActivityType.XP_CORRECTION -> Activity.XpCorrectionActivity(
                id = id,
                userId = userId,
                time = time,
                xp = xp
            )
        }
    }

}
