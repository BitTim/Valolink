/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoCompetitiveSeason.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.06.26, 02:27
 */

package dev.bittim.valolink.core.domain.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

data class ValoCompetitiveSeason(
    val uuid: Uuid,
    val season: Uuid,
    val rankTable: Uuid,
    val startTime: Instant,
    val endTime: Instant
)
