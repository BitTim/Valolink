/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       GetCurrentSeasonActivitiesForCurrentUserUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   22.06.26, 16:53
 */

package dev.bittim.valolink.feature.activity.domain.usecase

import dev.bittim.valolink.core.domain.model.Activity
import dev.bittim.valolink.core.domain.repo.ActivityRepo
import dev.bittim.valolink.core.domain.repo.AuthRepo
import dev.bittim.valolink.core.domain.repo.ValoSeasonRepo
import kotlinx.coroutines.flow.firstOrNull
import kotlin.time.Clock

class GetCurrentSeasonActivitiesForCurrentUserUseCase(
    private val authRepo: AuthRepo,
    private val valoSeasonRepo: ValoSeasonRepo,
    private val activityRepo: ActivityRepo
) {
    suspend operator fun invoke(locale: String? = null): List<Activity> {
        val userId = authRepo.getCurrentUserId() ?: return emptyList()
        val currentSeason = valoSeasonRepo.observe(Clock.System.now(), locale).firstOrNull() ?: return emptyList()

        return activityRepo.get(userId, currentSeason)
    }
}