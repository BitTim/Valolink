/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       Activity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 17:24
 */

package dev.bittim.valolink.core.domain.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

sealed class Activity {
    abstract val id: Uuid
    abstract val userId: Uuid
    abstract val time: Instant

    data class MatchActivity(
        override val id: Uuid,
        override val userId: Uuid,
        override val time: Instant,
        val xp: Int,
        val rr: Int?,
        val matchParticipant: MatchParticipant,
        val match: Match
    ) : Activity()

    data class XpCorrectionActivity(
        override val id: Uuid,
        override val userId: Uuid,
        override val time: Instant,
        val xp: Int
    ) : Activity()

    data class RrRefundActivity(
        override val id: Uuid,
        override val userId: Uuid,
        override val time: Instant,
        val rr: Int,
        val mode: ValoMode
    ) : Activity()
}
