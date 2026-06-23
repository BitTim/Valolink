/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       CalculateRrUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.06.26, 03:30
 */

package dev.bittim.valolink.feature.activity.domain.usecase

import dev.bittim.valolink.core.domain.model.Activity
import kotlin.time.Instant

class CalculateRrUseCase {
    operator fun invoke(
        activities: List<Activity>?,
        upToInclusive: Instant
    ): Int? {
        if (activities == null) return null

        val filteredActivities = activities.sortedBy { it.time }.filter { it.time != null && it.time <= upToInclusive && it.rr != null }
        return filteredActivities.sumOf { it.rr!! }
    }
}