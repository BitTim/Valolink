/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       CalculateRrBeforeTimeUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.06.26, 03:52
 */

package dev.bittim.valolink.feature.activity.domain.usecase

import dev.bittim.valolink.core.domain.model.Activity
import kotlin.time.Instant

class CalculateRrBeforeTimeUseCase {
    /**
     * Calculates the sum of `rr` values from activities up to (and including) a specified timestamp.
     *
     * @param activities The list of activities to process.
     * @param before The maximum timestamp to include in the sum.
     * @return The sum of `rr` values from activities with `time` less than or equal to `before`, or `null` if `activities` is `null`.
     */
    operator fun invoke(
        activities: List<Activity>?,
        before: Instant
    ): Int? {
        if (activities == null) return null

        val filteredActivities = activities.sortedBy { it.time }.takeWhile { it.time <= before }.filter { it.rr != null }
        return filteredActivities.sumOf { it.rr!! }
    }
}