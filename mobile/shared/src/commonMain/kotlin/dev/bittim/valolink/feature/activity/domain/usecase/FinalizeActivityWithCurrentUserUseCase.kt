/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       FinalizeActivityWithCurrentUserUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   25.08.26, 21:40
 */

package dev.bittim.valolink.feature.activity.domain.usecase

import dev.bittim.valolink.core.domain.model.ActivityDraft
import dev.bittim.valolink.core.domain.model.MatchDraft
import dev.bittim.valolink.core.domain.model.MatchParticipantDraft
import dev.bittim.valolink.core.domain.repo.ActivityRepo
import dev.bittim.valolink.feature.activity.domain.model.FinalizeActivityInput

class FinalizeActivityWithCurrentUserUseCase(
    private val activityRepo: ActivityRepo
) {
    /**
     * Finalizes an activity for the currently authenticated user and persists it with any associated match data.
     *
     * @param finalizeActivityInput The activity details and, when applicable, match details to finalize.
     * @throws IllegalStateException If no user is authenticated.
     */
    suspend operator fun invoke(finalizeActivityInput: FinalizeActivityInput) {
        val activityDraft = ActivityDraft(
            time = finalizeActivityInput.time,
            type = finalizeActivityInput.type,
            xp = finalizeActivityInput.xp,
            rr = finalizeActivityInput.rr,
            mode = finalizeActivityInput.mode
        )

        val matchBundle = when (finalizeActivityInput) {
            is FinalizeActivityInput.Match -> {
                val matchDraft = MatchDraft(
                    scoreA = finalizeActivityInput.scoreA,
                    scoreB = finalizeActivityInput.scoreB,
                    endReason = finalizeActivityInput.endReason,
                    isRanked = finalizeActivityInput.isRanked,
                    time = finalizeActivityInput.time,
                    map = finalizeActivityInput.map,
                    mode = finalizeActivityInput.mode
                )

                val participantDraft = MatchParticipantDraft(
                    visibleRr = finalizeActivityInput.visibleRr,
                    isOwner = true,
                    isTeamB = false
                )

                Pair(matchDraft, participantDraft)
            }
            else -> null
        }

        activityRepo.insert(activityDraft, matchBundle?.first, matchBundle?.second)
    }
}