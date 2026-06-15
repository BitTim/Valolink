/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ScoreChipState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   15.06.26, 17:52
 */

package dev.bittim.valolink.feature.activity.ui.components.match

import dev.bittim.valolink.core.domain.model.MatchOutcome

data class ScoreChipState(
    val outcome: MatchOutcome,
    val wasSurrender: Boolean,
    val score: String?
)
