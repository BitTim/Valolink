/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityDraft.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   25.08.26, 21:10
 */

package dev.bittim.valolink.core.domain.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

data class ActivityDraft(
    val time: Instant,
    val type: ActivityType,
    val xp: Int,
    val rr: Int?,
    val mode: Uuid?
)
