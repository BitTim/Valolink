/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchOutcome.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   29.08.26, 18:09
 */

package dev.bittim.valolink.core.domain.model

enum class MatchOutcome {
    Draw,
    Win,
    Loss;

    companion object {
        fun fromScore(scoreA: Int?, scoreB: Int?, isTeamB: Boolean, surrender: MatchEndReason, modeCategory: ValoModeCategory): MatchOutcome {
            return when (surrender) {
                MatchEndReason.SURRENDER_A -> if (isTeamB) Win else Loss
                MatchEndReason.SURRENDER_B -> if (isTeamB) Loss else Win
                MatchEndReason.COMPLETED -> {
                    if (scoreA == null) return Draw

                    when (modeCategory) {
                        ValoModeCategory.Unknown, ValoModeCategory.Tutorial, ValoModeCategory.Range -> Draw

                        ValoModeCategory.Standard, ValoModeCategory.TDM, ValoModeCategory.Skirmish -> when {
                            scoreB == null -> Draw
                            scoreA > scoreB -> if (isTeamB) Loss else Win
                            scoreA < scoreB -> if (isTeamB) Win else Loss
                            else -> Draw
                        }

                        ValoModeCategory.Deathmatch -> if (scoreA == 1) Win else Draw
                    }
                }
            }
        }
    }
}