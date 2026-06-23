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
    operator fun invoke(
        activities: List<Activity>?,
        upToInclusive: Uuid
    ): Int? {
        if (activities == null) return null

        val targetActivity = activities.firstOrNull { it.id == upToInclusive } ?: return null
        val targetTime = targetActivity.time ?: return null

        val filteredActivities = activities.sortedBy { it.time }.filter {
            it.time != null && it.time <= targetTime && it.rr != null
        }
        return filteredActivities.sumOf { it.rr!! }
    }
}