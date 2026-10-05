/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityListItemState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.09.26, 19:36
 */

package dev.bittim.valolink.feature.activity.ui.screen.list.state

import dev.bittim.valolink.feature.activity.ui.components.match.MatchCardState
import kotlin.uuid.Uuid

sealed class ActivityListItemState {
    abstract val id: Uuid

    data class MatchCard(override val id: Uuid, val state: MatchCardState): ActivityListItemState()
    data class XpCorrection(override val id: Uuid, val xp: Int): ActivityListItemState()
    data class RrRefund(override val id: Uuid, val rr: Int, val modeName: String): ActivityListItemState()
}