/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchIconState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   04.06.26, 18:28
 */

package dev.bittim.valolink.feature.activity.ui.components.match

import dev.bittim.valolink.core.domain.model.MatchOutcome

data class MatchIconState(
    val outcome: MatchOutcome,
    val mapImageUrl: String?,
    val iconUrl: String?,
    val rrChipState: RrChipState?
)
