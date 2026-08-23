/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       FinalizeActivityWithCurrentUserUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.08.26, 16:02
 */

package dev.bittim.valolink.feature.activity.domain.usecase

import dev.bittim.valolink.core.domain.model.Activity
import dev.bittim.valolink.core.domain.model.Match
import dev.bittim.valolink.core.domain.model.MatchParticipant
import kotlin.uuid.Uuid

class FinalizeActivityWithCurrentUserUseCase {
    operator fun invoke(
        userId: Uuid
    ): Pair<Activity, Pair<Match, MatchParticipant>?> {
        val activity = Activity(
            id = Uuid.random(),
            userId =
        )
    }
}