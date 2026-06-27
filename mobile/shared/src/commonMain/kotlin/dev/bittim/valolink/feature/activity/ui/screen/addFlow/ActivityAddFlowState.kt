/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.06.26, 01:41
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow

import dev.bittim.valolink.core.domain.model.MatchEndReason
import dev.bittim.valolink.feature.activity.ui.components.map.MapCardState
import dev.bittim.valolink.feature.activity.ui.components.match.MatchCardState
import dev.bittim.valolink.feature.activity.ui.components.mode.ModeCardState
import dev.bittim.valolink.feature.activity.ui.components.rank.RankCardState
import org.jetbrains.compose.resources.StringResource
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class ActivityAddFlowState(
    val step: ActivityAddFlowStep = ActivityAddFlowStep.ModeStep,
    val modeCardStates: List<ModeCardState>? = null,
    val mapCardStates: List<MapCardState>? = null,
    val rankCardStates: List<RankCardState>? = null,
    val isPlacementScoreType: Boolean = false,
    val supportsRanked: Boolean = false,
    val hasRankPlacement: Boolean = false,

    val enableModeContinueButton: Boolean = false,
    val enableMapContinueButton: Boolean = false,
    val enableScoreContinueButton: Boolean = false,
    val enableRankContinueButton: Boolean = false,
    val enableResultContinueButton: Boolean = false,

    val dateTimePickerVisible: Boolean = false,

    val scoreAError: StringResource? = null,
    val scoreBError: StringResource? = null,
    val xpError: StringResource? = null,
    val rrError: StringResource? = null,

    val matchCardState: MatchCardState = MatchCardState.Empty,
    val modeUuid: Uuid? = null,
    val mapUuid: Uuid? = null,
    val scoreA: Int? = null,
    val scoreB: Int? = null,
    val surrender: MatchEndReason = MatchEndReason.COMPLETED,
    val xp: Int? = null,
    val isRankedSelected: Boolean = false,
    val rr: Int? = null,
    val time: Instant = Clock.System.now()
)
