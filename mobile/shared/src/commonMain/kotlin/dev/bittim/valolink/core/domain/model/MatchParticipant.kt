/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchParticipant.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 17:07
 */

package dev.bittim.valolink.core.domain.model

import kotlin.uuid.Uuid

data class MatchParticipant(
    val userId: Uuid,
    val visibleRr: Int?,
    val isOwner: Boolean,
    val isTeamB: Boolean
)
