/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchOutcomeFromScoreUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   15.06.26, 20:54
 */

package dev.bittim.valolink.feature.activity.domain.usecase

import dev.bittim.valolink.core.domain.model.MatchEndReason
import dev.bittim.valolink.core.domain.model.MatchOutcome
import dev.bittim.valolink.core.domain.model.ValoModeCategory

class MatchOutcomeFromScoreUseCase {
    operator fun invoke(scoreA: Int?, scoreB: Int?, surrender: MatchEndReason, modeCategory: ValoModeCategory): MatchOutcome? {
        return when (surrender) {
            MatchEndReason.SURRENDER_A -> MatchOutcome.Loss
            MatchEndReason.SURRENDER_B -> MatchOutcome.Win
            MatchEndReason.COMPLETED -> {
                if (scoreA == null) return null

                when (modeCategory) {
                    ValoModeCategory.Unknown, ValoModeCategory.Tutorial, ValoModeCategory.Range -> null

                    ValoModeCategory.Standard, ValoModeCategory.TDM, ValoModeCategory.Skirmish -> when {
                        scoreB == null -> null
                        scoreA > scoreB -> MatchOutcome.Win
                        scoreA < scoreB -> MatchOutcome.Loss
                        else -> MatchOutcome.Draw
                    }

                    ValoModeCategory.Deathmatch -> if (scoreA == 1) MatchOutcome.Win else MatchOutcome.Draw
                }
            }
        }
    }
}