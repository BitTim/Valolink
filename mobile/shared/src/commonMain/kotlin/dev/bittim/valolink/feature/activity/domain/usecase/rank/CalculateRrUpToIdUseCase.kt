/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       CalculateRrUpToIdUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 19:07
 */

package dev.bittim.valolink.feature.activity.domain.usecase.rank

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
            val activityModeUuid = when(it) {
                is Activity.MatchActivity -> it.match.mode.uuid
                is Activity.RrRefundActivity -> it.mode.uuid
                else -> false
            }

            activityModeUuid == modeUuid
        }.sortedBy { it.time }
        val lastIndex = sortedActivities.indexOfFirst { it.id == upToInclusive }
        val filteredActivities = sortedActivities.take(lastIndex + 1).filter {
            when(it) {
                is Activity.MatchActivity -> it.rr != null
                is Activity.RrRefundActivity -> true
                else -> false
            }
        }
        return if (filteredActivities.isEmpty()) null else filteredActivities.sumOf {
            when(it) {
                is Activity.MatchActivity -> it.rr!!
                is Activity.RrRefundActivity -> it.rr
                else -> 0
            }
        }
    }
}