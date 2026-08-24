/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       FinalizeActivityWithCurrentUserUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.08.26, 17:22
 */

package dev.bittim.valolink.feature.activity.domain.usecase

import dev.bittim.valolink.core.domain.model.Activity
import dev.bittim.valolink.core.domain.model.Match
import dev.bittim.valolink.core.domain.model.MatchParticipant
import dev.bittim.valolink.core.domain.repo.ActivityRepo
import dev.bittim.valolink.core.domain.repo.AuthRepo
import dev.bittim.valolink.core.domain.repo.MatchRepo
import dev.bittim.valolink.feature.activity.domain.model.FinalizeActivityInput
import dev.bittim.valolink.feature.activity.domain.model.MatchBundle
import kotlin.uuid.Uuid

class FinalizeActivityWithCurrentUserUseCase(
    private val authRepo: AuthRepo,
    private val activityRepo: ActivityRepo,
    private val matchRepo: MatchRepo
) {
    suspend operator fun invoke(finalizeActivityInput: FinalizeActivityInput) {
        val userId = authRepo.getCurrentUserId() ?: return // TODO: Replace with Error

        val activity = Activity(
            id = Uuid.random(),
            userId = userId,
            time = finalizeActivityInput.time,
            type = finalizeActivityInput.type,
            xp = finalizeActivityInput.xp,
            rr = finalizeActivityInput.rr,
            mode = finalizeActivityInput.mode
        )

        val matchBundle = when (finalizeActivityInput) {
            is FinalizeActivityInput.Match -> {
                val match = Match(
                    id = Uuid.random(),
                    scoreA = finalizeActivityInput.scoreA,
                    scoreB = finalizeActivityInput.scoreB,
                    endReason = finalizeActivityInput.endReason,
                    isRanked = finalizeActivityInput.isRanked,
                    time = finalizeActivityInput.time,
                    map = finalizeActivityInput.map,
                    mode = finalizeActivityInput.mode
                )

                val participant = MatchParticipant(
                    userId = userId,
                    activity = activity.id,
                    match = match.id,
                    visibleRr = finalizeActivityInput.visibleRr,
                    isOwner = true,
                    isTeamB = false
                )

                MatchBundle(match, participant)
            }
            else -> null
        }

        activityRepo.upsert(activity)
        matchBundle?.let { matchRepo.upsert(it.match, it.participant) }
    }
}