/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       CalculateRrUpToIdUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.06.26, 03:52
 */

package dev.bittim.valolink.feature.activity.domain.usecase

import dev.bittim.valolink.core.domain.model.Activity
import kotlin.uuid.Uuid

class CalculateRrUpToIdUseCase {
    /**
     * Calculates the sum of `rr` values from activities up to (and including) a specified identifier.
     *
     * @param activities The list of activities to process.
     * @param upToInclusive The maximum activity identifier to include in the sum.
     * @return The sum of `rr` values from activities sorted by time up to `id` equal to `upToInclusive`, or `null` if `activities` is `null`.
     */
    operator fun invoke(
        activities: List<Activity>?,
        upToInclusive: Uuid
    ): Int? {
        if (activities == null) return null

        val sortedActivities = activities.sortedBy { it.time }
        val lastIndex = sortedActivities.indexOfFirst { it.id == upToInclusive }
        val filteredActivities = sortedActivities.take(lastIndex + 1).filter { it.rr != null }
        return filteredActivities.sumOf { it.rr!! }
    }
}