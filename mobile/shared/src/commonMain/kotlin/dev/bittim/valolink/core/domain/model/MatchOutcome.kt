/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchOutcome.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 20:02
 */

package dev.bittim.valolink.core.domain.model

enum class MatchOutcome {
    Draw,
    Win,
    Loss;

    companion object {
        fun fromScore(scoreA: Int?, scoreB: Int?, surrender: MatchEndReason, modeCategory: ValoModeCategory): MatchOutcome {
            return when (surrender) {
                MatchEndReason.SURRENDER_A -> Loss
                MatchEndReason.SURRENDER_B -> Win
                MatchEndReason.COMPLETED -> {
                    if (scoreA == null) return Draw

                    when (modeCategory) {
                        ValoModeCategory.Unknown, ValoModeCategory.Tutorial, ValoModeCategory.Range -> Draw

                        ValoModeCategory.Standard, ValoModeCategory.TDM, ValoModeCategory.Skirmish -> when {
                            scoreB == null -> Draw
                            scoreA > scoreB -> Win
                            scoreA < scoreB -> Loss
                            else -> Draw
                        }

                        ValoModeCategory.Deathmatch -> if (scoreA == 1) Win else Draw
                    }
                }
            }
        }
    }
}