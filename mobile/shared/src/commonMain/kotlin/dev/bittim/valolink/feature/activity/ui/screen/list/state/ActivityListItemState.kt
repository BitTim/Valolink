/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityListItemState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   29.08.26, 17:12
 */

package dev.bittim.valolink.feature.activity.ui.screen.list.state

import dev.bittim.valolink.feature.activity.ui.components.match.MatchCardState

sealed class ActivityListItemState {
    data class MatchCard(val state: MatchCardState): ActivityListItemState()
    data class XpCorrection(val xp: Int): ActivityListItemState()
    data class RrRefund(val rr: Int, val modeName: String): ActivityListItemState()
}