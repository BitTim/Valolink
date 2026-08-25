/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchParticipantDraft.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   25.08.26, 21:44
 */

package dev.bittim.valolink.core.domain.model

data class MatchParticipantDraft(
    val visibleRr: Int?,
    val isOwner: Boolean,
    val isTeamB: Boolean
)
