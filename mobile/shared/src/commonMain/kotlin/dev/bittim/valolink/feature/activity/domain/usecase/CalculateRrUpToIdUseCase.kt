/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       CalculateRrUpToIdUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.06.26, 19:21
 */

package dev.bittim.valolink.feature.activity.domain.usecase

import dev.bittim.valolink.core.domain.model.Activity
import kotlin.uuid.Uuid

class CalculateRrUpToIdUseCase {
    /**
     * Calculates the sum of `rr` values for matching activities up to a specified identifier.
     *
     * @param activities The activities to process.
     * @param modeUuid The activity mode to match.
     * @param upToInclusive The activity identifier to include up to.
     * @return The sum of `rr` values for matching activities up to `upToInclusive`, or `null` if no matching `rr` values are found or `activities` is `null`.
     */
    operator fun invoke(
        activities: List<Activity>?,
        modeUuid: Uuid?,
        upToInclusive: Uuid
    ): Int? {
        if (activities == null) return null

        val sortedActivities = activities.filter {
            it.mode == modeUuid
        }.sortedBy { it.time }
        val lastIndex = sortedActivities.indexOfFirst { it.id == upToInclusive }
        val filteredActivities = sortedActivities.take(lastIndex + 1).filter { it.rr != null }
        return if (filteredActivities.isEmpty()) null else filteredActivities.sumOf { it.rr!! }
    }
}