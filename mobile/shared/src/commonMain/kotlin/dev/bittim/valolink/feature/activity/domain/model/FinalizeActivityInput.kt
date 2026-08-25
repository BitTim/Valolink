/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       FinalizeActivityInput.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.08.26, 14:27
 */

package dev.bittim.valolink.feature.activity.domain.model

import dev.bittim.valolink.core.domain.model.ActivityType
import dev.bittim.valolink.core.domain.model.MatchEndReason
import kotlin.time.Instant
import kotlin.uuid.Uuid

sealed interface FinalizeActivityInput {
    val type: ActivityType
    val time: Instant
    val xp: Int
    val rr: Int?
    val mode: Uuid?

    data class Match(
        override val time: Instant,
        override val xp: Int,
        override val rr: Int?,
        override val mode: Uuid,

        val scoreA: Int,
        val scoreB: Int?,
        val endReason: MatchEndReason,
        val isRanked: Boolean,
        val map: Uuid,

        val visibleRr: Int?,
    ) : FinalizeActivityInput {
        override val type: ActivityType get() = ActivityType.MATCH
    }

    data class XpCorrection(
        override val time: Instant,
        override val xp: Int,
    ) : FinalizeActivityInput {
        override val type: ActivityType get() = ActivityType.XP_CORRECTION
        override val rr: Int? get() = null
        override val mode: Uuid? get() = null
    }

    data class RrRefund(
        override val time: Instant,
        override val rr: Int?,
        override val mode: Uuid?,
    ) : FinalizeActivityInput {
        override val type: ActivityType get() = ActivityType.RR_REFUND
        override val xp: Int get() = 0
    }
}