/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       GetSeasonActivitiesForCurrentUserByTimeUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.06.26, 17:48
 */

package dev.bittim.valolink.feature.activity.domain.usecase

import dev.bittim.valolink.core.domain.model.Activity
import dev.bittim.valolink.core.domain.repo.ActivityRepo
import dev.bittim.valolink.core.domain.repo.AuthRepo
import dev.bittim.valolink.core.domain.repo.ValoSeasonRepo
import kotlinx.coroutines.flow.firstOrNull
import kotlin.time.Instant

class GetSeasonActivitiesForCurrentUserByTimeUseCase(
    private val authRepo: AuthRepo,
    private val valoSeasonRepo: ValoSeasonRepo,
    private val activityRepo: ActivityRepo
) {
    suspend operator fun invoke(time: Instant, locale: String? = null): List<Activity> {
        val userId = authRepo.getCurrentUserId() ?: return emptyList()
        val season = valoSeasonRepo.observe(time, locale).firstOrNull() ?: return emptyList()

        return activityRepo.get(userId, season)
    }
}