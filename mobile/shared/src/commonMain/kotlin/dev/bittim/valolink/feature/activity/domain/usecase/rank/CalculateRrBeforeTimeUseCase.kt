/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       CalculateRrBeforeTimeUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.06.26, 13:23
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

        val filteredActivities = activities.filter {
            it.mode == modeUuid
        }.sortedBy { it.time }.takeWhile { it.time <= before }.filter { it.rr != null }
        return if(filteredActivities.isEmpty()) null else filteredActivities.sumOf { it.rr!! }
    }
}