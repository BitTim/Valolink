/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.08.26, 03:21
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.state

import dev.bittim.valolink.core.domain.model.MatchOutcome
import dev.bittim.valolink.core.domain.model.Rank
import dev.bittim.valolink.feature.activity.ui.components.map.MapCardState
import dev.bittim.valolink.feature.activity.ui.components.match.MatchCardState
import dev.bittim.valolink.feature.activity.ui.components.mode.ModeCardState
import dev.bittim.valolink.feature.activity.ui.components.rank.RankCardState
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowStep

data class ActivityAddFlowState(
    val step: ActivityAddFlowStep = ActivityAddFlowStep.ModeStep,
    val form: ActivityAddFlowFormState = ActivityAddFlowFormState(),
    val modeCardStates: List<ModeCardState>? = null,
    val mapCardStates: List<MapCardState>? = null,
    val rankCardStates: List<RankCardState>? = null,
    val isPlacementScoreType: Boolean = false,

    val showRankModifier: Boolean = false,
    val dateTimePickerVisible: Boolean = false,
    val matchOutcome: MatchOutcome? = null,
    val currentRank: Rank? = null,
    val rrDelta: Int? = null,
    val matchCardState: MatchCardState = MatchCardState.Empty,

    val isFinalizing: Boolean = false
)

val ActivityAddFlowState.canRrRefund: Boolean
    get() = currentRank != null && currentRank.rank.tier != 0 && !form.rankPlacement

val ActivityAddFlowState.canContinueFromMode: Boolean
    get() = modeCardStates != null && form.modeUuid != null

val ActivityAddFlowState.canContinueFromMap: Boolean
    get() = mapCardStates != null && form.mapUuid != null

val ActivityAddFlowState.canContinueFromScore: Boolean
    get() = form.scoreA != null && form.scoreAError == null &&
        (isPlacementScoreType || form.scoreB != null && form.scoreBError == null)

val ActivityAddFlowState.canContinueFromRank: Boolean
    get() = (form.rankPlacement && form.selectedRankTier != null && form.placementRr != null && form.placementRrError == null) ||
        !form.rankPlacement || (form.visibleRrDelta != null && form.rrDeltaError == null)

val ActivityAddFlowState.canContinueFromXp: Boolean
    get() = form.xp != null && form.xpError == null && !isFinalizing

val ActivityAddFlowState.canContinueFromXpCorrection: Boolean
    get() = form.xp != null && form.xpError == null && !isFinalizing

val ActivityAddFlowState.canContinueFromRrRefund: Boolean
    get() = form.visibleRrDelta != null && form.rrDeltaError == null && !isFinalizing