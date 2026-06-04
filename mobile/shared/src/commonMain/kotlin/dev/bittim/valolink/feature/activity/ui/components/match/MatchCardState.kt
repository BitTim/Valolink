/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchCardState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   04.06.26, 11:53
 */

package dev.bittim.valolink.feature.activity.ui.components.match

data class MatchCardState(
    val iconState: MatchIconState,
    val scoreChipState: ScoreChipState,
    val modeName: String,
    val mapName: String,
    val time: String,
    val xp: Int,
)
