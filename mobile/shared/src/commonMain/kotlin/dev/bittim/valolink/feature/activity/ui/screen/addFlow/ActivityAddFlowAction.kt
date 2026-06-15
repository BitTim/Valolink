/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowAction.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   15.06.26, 20:19
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow

import dev.bittim.valolink.core.domain.model.MatchEndReason
import kotlin.uuid.Uuid

sealed interface ActivityAddFlowAction {
    data object Back: ActivityAddFlowAction
    data class ModeSelected(val uuid: Uuid) : ActivityAddFlowAction
    data object ModeContinue : ActivityAddFlowAction
    data class MapSelected(val uuid: Uuid) : ActivityAddFlowAction
    data object MapContinue : ActivityAddFlowAction
    data class ScoreAChanged(val rawScore: String?) : ActivityAddFlowAction
    data class ScoreBChanged(val rawScore: String?) : ActivityAddFlowAction
    data class SurrenderChanged(val reason: MatchEndReason) : ActivityAddFlowAction
}