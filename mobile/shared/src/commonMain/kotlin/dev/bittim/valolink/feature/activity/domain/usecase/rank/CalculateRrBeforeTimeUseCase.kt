/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       CalculateRrBeforeTimeUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 18:48
 */

package dev.bittim.valolink.feature.activity.domain.usecase.rank

import dev.bittim.valolink.core.domain.model.Activity
import kotlin.time.Instant
import kotlin.uuid.Uuid

class CalculateRrBeforeTimeUseCase {
    /**
     * Calculates the sum of `rr` values for matching activities up to a given timestamp.
     *
     * @param activities The activities to process.
     * @param modeUuid The mode to match against each activity's mode.
     * @param before The latest timestamp to include.
     * @return The sum of `rr` values for activities with a matching mode and `time` less than or equal to `before`, or `null` if no matching activities contribute to the sum.
     */
    operator fun invoke(
        activities: List<Activity>?,
        modeUuid: Uuid?,
        before: Instant
    ): Int? {
        if (activities == null) return null

        var totalRr = 0
        var hasRr = false

        for(activity in activities) {
            val (activityModeUuid, activityRr) = when (activity) {
                is Activity.MatchActivity -> activity.match.mode.uuid to activity.rr
                is Activity.RrRefundActivity -> activity.mode.uuid to activity.rr
                else -> continue
            }

            if (activityModeUuid == modeUuid && activity.time <= before && activityRr != null) {
                totalRr += activityRr
                hasRr = true
            }
        }

        return totalRr.takeIf { hasRr }
    }
}
