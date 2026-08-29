/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       GetSeasonActivitiesForCurrentUserByTimeUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   29.08.26, 18:18
 */

package dev.bittim.valolink.feature.activity.domain.usecase

import dev.bittim.valolink.core.domain.model.Activity
import dev.bittim.valolink.core.domain.repo.*
import kotlinx.coroutines.flow.firstOrNull
import kotlin.time.Clock
import kotlin.time.Instant

class GetSeasonActivitiesForCurrentUserByTimeUseCase(
    private val authRepo: AuthRepo,
    private val valoSeasonRepo: ValoSeasonRepo,
    private val activityRepo: ActivityRepo,
    private val modeRepo: ValoModeRepo,
    private val mapRepo: ValoMapRepo
) {
    /**
     * Gets the current user's activities for the season active at the specified time.
     *
     * @param time The point in time used to determine the active season.
     * @param locale The locale used when resolving the season.
     * @return The activities for the current user in the active season, or an empty list if no current user or season is available.
     */
    suspend operator fun invoke(time: Instant = Clock.System.now(), locale: String? = null): List<Activity> {
        val userId = authRepo.getCurrentUserId() ?: return emptyList()
        val season = valoSeasonRepo.observe(time, locale).firstOrNull() ?: return emptyList()

        return activityRepo.get(userId, season).mapNotNull { dto ->
            val matchParticipantDto = dto.matchParticipants
            val matchParticipant = matchParticipantDto?.toModel()
            val matchDto = matchParticipantDto?.matches

            val modeId = matchDto?.mode ?: dto.mode
            val mode = modeId?.let { modeRepo.get(it, locale) }
            val map = matchDto?.let { mapRepo.get(it.map, locale) }

            val match = if (matchDto != null && map != null && mode != null) {
                matchDto.toModel(map, mode)
            } else null

            dto.toModel(matchParticipant, match, mode)
        }.sortedByDescending { it.time }
    }
}