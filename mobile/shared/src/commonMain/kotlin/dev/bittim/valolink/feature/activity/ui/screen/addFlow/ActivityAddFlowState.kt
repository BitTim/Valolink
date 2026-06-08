/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   08.06.26, 21:48
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow

import dev.bittim.valolink.core.domain.model.SimpleValoMap
import dev.bittim.valolink.core.domain.model.ValoMode
import dev.bittim.valolink.feature.activity.ui.components.match.MatchCardState
import kotlin.uuid.Uuid

data class ActivityAddFlowState(
    val step: ActivityAddFlowStep = ActivityAddFlowStep.ModeStep,
    val modes: List<ValoMode>? = null,
    val maps: List<SimpleValoMap>? = null,

    val matchCardState: MatchCardState = MatchCardState.Empty,
    val selectedModeUuid: Uuid? = null,
    val selectedMapUuid: Uuid? = null
)
