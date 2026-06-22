/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoSeason.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   22.06.26, 04:02
 */

package dev.bittim.valolink.core.domain.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

data class ValoSeason(
    val uuid: Uuid,
    val displayName: String,
    val episodeDisplayName: String?,
    val title: String?,
    val startTime: Instant,
    val endTime: Instant
)
