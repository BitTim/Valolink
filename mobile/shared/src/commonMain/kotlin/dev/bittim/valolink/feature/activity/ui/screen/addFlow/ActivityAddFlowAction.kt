/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowAction.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.06.26, 14:23
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow

import dev.bittim.valolink.core.domain.model.MatchEndReason
import kotlin.uuid.Uuid

sealed interface ActivityAddFlowAction {
    data object Back: ActivityAddFlowAction
    data class ModeSelected(val uuid: Uuid) : ActivityAddFlowAction
    data class RankedChanged(val selected: Boolean) : ActivityAddFlowAction
    data object ModeContinue : ActivityAddFlowAction
    data class MapSelected(val uuid: Uuid) : ActivityAddFlowAction
    data object MapContinue : ActivityAddFlowAction
    data class ScoreAChanged(val rawScore: String?) : ActivityAddFlowAction
    data class ScoreBChanged(val rawScore: String?) : ActivityAddFlowAction
    data class SurrenderChanged(val reason: MatchEndReason) : ActivityAddFlowAction
    data object ScoreContinue : ActivityAddFlowAction
    data class RrDeltaChanged(val rawRr: String?) : ActivityAddFlowAction
    data class XpChanged(val rawXp: String?) : ActivityAddFlowAction
    data object ChangeTime : ActivityAddFlowAction
    data object DateTimePickerDismiss : ActivityAddFlowAction
    data class DateTimeSelected(val dateMillis: Long, val hour: Int, val minute: Int) : ActivityAddFlowAction
    data object XpFinish : ActivityAddFlowAction
}