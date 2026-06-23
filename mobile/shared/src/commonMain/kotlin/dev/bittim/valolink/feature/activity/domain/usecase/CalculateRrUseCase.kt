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
import kotlin.uuid.Uuid

class CalculateRrUseCase {
    /**
     * Calculates the sum of `rr` values from activities up to (and including) a specified identifier.
     *
     * @param activities The list of activities to process.
     * @param upToInclusive The maximum activity identifier to include in the sum.
     * @return The sum of `rr` values from activities with `id` less than or equal to `upToInclusive`, or `null` if `activities` is `null`.
     */
    operator fun invoke(
        activities: List<Activity>?,
        upToInclusive: Uuid
    ): Int? {
        if (activities == null) return null

        val filteredActivities = activities.sortedBy { it.time }.filter { it.id <= upToInclusive && it.rr != null }
        return filteredActivities.sumOf { it.rr!! }
    }
}