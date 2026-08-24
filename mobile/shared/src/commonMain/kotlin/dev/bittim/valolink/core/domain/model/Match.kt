/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       Match.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.08.26, 15:36
 */

package dev.bittim.valolink.core.domain.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

data class Match(
    val id: Uuid,
    val scoreA: Int,
    val scoreB: Int?,
    val endReason: MatchEndReason,
    val isRanked: Boolean,
    val time: Instant,
    val map: Uuid,
    val mode: Uuid
)
