/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowAction.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   18.08.26, 20:32
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow

import dev.bittim.valolink.core.domain.model.MatchEndReason
import kotlin.uuid.Uuid

sealed interface ActivityAddFlowAction {
    enum class ScoreTeam { A, B }

    data object Back: ActivityAddFlowAction
    data object ToXpCorrection: ActivityAddFlowAction
    data object ToRrRefund: ActivityAddFlowAction
    data class ModeSelected(val uuid: Uuid) : ActivityAddFlowAction
    data class RankedChanged(val selected: Boolean) : ActivityAddFlowAction
    data object ModeContinue : ActivityAddFlowAction
    data class MapSelected(val uuid: Uuid) : ActivityAddFlowAction
    data object MapContinue : ActivityAddFlowAction
    data class ScoreChanged(val team: ScoreTeam, val rawScore: String?) : ActivityAddFlowAction
    data class SurrenderChanged(val reason: MatchEndReason) : ActivityAddFlowAction
    data object ScoreContinue : ActivityAddFlowAction
    data class RankPlacementChanged(val placement: Boolean) : ActivityAddFlowAction
    data class RankSelected(val tier: Int) : ActivityAddFlowAction
    data class RrDeltaChanged(val rawRr: String?) : ActivityAddFlowAction
    data object RankContinue : ActivityAddFlowAction
    data class XpChanged(val rawXp: String?, val allowNegative: Boolean = false) : ActivityAddFlowAction
    data object ChangeTime : ActivityAddFlowAction
    data object DateTimePickerDismiss : ActivityAddFlowAction
    data class DateTimeSelected(val dateMillis: Long, val hour: Int, val minute: Int) : ActivityAddFlowAction
    data object XpFinish : ActivityAddFlowAction
}
