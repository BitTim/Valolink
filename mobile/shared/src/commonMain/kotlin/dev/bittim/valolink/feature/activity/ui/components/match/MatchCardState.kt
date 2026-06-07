/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchCardState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.06.26, 20:53
 */

package dev.bittim.valolink.feature.activity.ui.components.match

import dev.bittim.valolink.core.domain.model.MatchOutcome

data class MatchCardState(
    val iconState: MatchIconState,
    val scoreChipState: ScoreChipState,
    val modeName: String,
    val mapName: String,
    val time: String,
    val xp: Int,
) {
    companion object {
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
                score = "0 - 0"
            ),
            modeName = "No mode selected", // TODO: Localize
            mapName = "No map",
            time = "No time",
            xp = 0
        )
    }
}
