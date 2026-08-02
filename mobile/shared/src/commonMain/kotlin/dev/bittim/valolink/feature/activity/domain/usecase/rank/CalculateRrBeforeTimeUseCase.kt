/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       CalculateRrBeforeTimeUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   01.08.26, 12:58
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

        activities.forEach { activity ->
            if (activity.mode == modeUuid && activity.time <= before && activity.rr != null) {
                totalRr += activity.rr
                hasRr = true
            }
        }

        return totalRr.takeIf { hasRr }
    }
}
