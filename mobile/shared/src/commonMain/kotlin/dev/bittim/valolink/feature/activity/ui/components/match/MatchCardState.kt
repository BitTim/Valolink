/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchCardState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 20:27
 */

package dev.bittim.valolink.feature.activity.ui.components.match

import dev.bittim.valolink.core.domain.extension.toLocalizedString
import dev.bittim.valolink.core.domain.model.Activity
import dev.bittim.valolink.core.domain.model.MatchEndReason
import dev.bittim.valolink.core.domain.model.MatchOutcome
import dev.bittim.valolink.feature.activity.domain.logic.formatScore

data class MatchCardState(
    val iconState: MatchIconState,
    val scoreChipState: ScoreChipState,
    val modeName: String,
    val mapName: String,
    val time: String,
    val xp: Int?,
) {
    companion object {
        fun fromActivity(activity: Activity.MatchActivity): MatchCardState {
            // TODO: Get and display ranks for ranked games

            val score = formatScore(
                activity.match.scoreA,
                activity.match.scoreB,
                activity.match.mode.category,
                activity.matchParticipant.isTeamB
            )

            val matchOutcome = MatchOutcome.fromScore(
                activity.match.scoreA,
                activity.match.scoreB,
                activity.match.endReason,
                activity.match.mode.category,
            )

            val wasSurrender = activity.match.endReason == MatchEndReason.SURRENDER_A || activity.match.endReason == MatchEndReason.SURRENDER_B

            return MatchCardState(
                iconState = MatchIconState(
                    outcome = matchOutcome,
                    mapImageUrl = activity.match.map.splash,
                    iconUrl = activity.match.mode.displayIcon,
                    rrChipState = activity.matchParticipant.visibleRr?.let { rr ->
                        RrChipState(
                            rr = rr,
                            rankChanged = false
                        )
                    }
                ),
                scoreChipState = ScoreChipState(
                    outcome = matchOutcome,
                    wasSurrender = wasSurrender,
                    score = score
                ),
                modeName = activity.match.mode.displayName,
                mapName = activity.match.map.displayName,
                time = activity.match.time.toLocalizedString(),
                xp = activity.xp
            )
        }

        val Empty = MatchCardState(
            iconState = MatchIconState(
                outcome = MatchOutcome.Draw,
                mapImageUrl = null,
                iconUrl = null,
                rrChipState = null
            ),
            scoreChipState = ScoreChipState(
                outcome = MatchOutcome.Draw,
                wasSurrender = false,
                score = ""
            ),
            modeName = "",
            mapName = "",
            time = "",
            xp = null
        )
    }
}
