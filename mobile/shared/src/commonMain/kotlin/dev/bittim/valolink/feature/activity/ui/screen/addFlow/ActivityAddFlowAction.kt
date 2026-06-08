/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowAction.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   08.06.26, 21:39
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow

import dev.bittim.valolink.core.domain.model.SimpleValoMap
import dev.bittim.valolink.core.domain.model.ValoMode

sealed interface ActivityAddFlowAction {
    data object Back: ActivityAddFlowAction
    data class ModeSelected(val mode: ValoMode): ActivityAddFlowAction
    data class MapSelected(val map: SimpleValoMap): ActivityAddFlowAction
}