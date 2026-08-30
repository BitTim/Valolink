/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowScreen.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.08.26, 03:17
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.flowScaffold.FlowScaffold
import dev.bittim.valolink.feature.activity.ui.components.match.MatchCard
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.state.*
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps.*
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps.rank.RankChangeState
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps.rank.RankPlacementState
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps.rank.RankStep
import org.jetbrains.compose.resources.stringResource

/**
 * Renders the activity-entry flow and forwards user interactions as actions.
 *
 * @param state The current state of the activity-entry flow.
 * @param onAction Callback invoked when the user performs an action.
 */
@Composable
@Preview
fun ActivityAddFlowScreen(
    state: ActivityAddFlowState = ActivityAddFlowState(),
    onAction: (ActivityAddFlowAction) -> Unit = {},
) {
    FlowScaffold(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.displayCutout)
            .windowInsetsPadding(WindowInsets.systemBars)
            .windowInsetsPadding(WindowInsets.ime),
        padding = PaddingValues(horizontal = Spacing.l),
        step = state.step,
        cancellable = true,
        onBack = { onAction(ActivityAddFlowAction.Back) },
        hero = { step ->
            when (step) {
                ActivityAddFlowStep.XpCorrectionStep -> { }
                ActivityAddFlowStep.RrRefundStep -> { }
                else -> {
                    MatchCard(
                        modifier = Modifier.fillMaxWidth(),
                        state = state.matchCardState
                    )
                }
            }
        },
        heroKey = { s -> if (s == ActivityAddFlowStep.XpCorrectionStep || s == ActivityAddFlowStep.RrRefundStep) s else "match" },
        content = { step, padding ->
            when (step) {
                ActivityAddFlowStep.ModeStep -> {
                    ModeStep(
                        modifier = Modifier.padding(padding),
                        selectedModeUuid = state.form.modeUuid,
                        modeCardStates = state.modeCardStates,
                        enableContinueButton = state.canContinueFromMode,
                        enableRrRefundOption = state.canRrRefund,
                        isRankedSelected = state.form.isRankedSelected,
                        onAction = onAction
                    )
                }
                ActivityAddFlowStep.MapStep -> {
                    MapStep(
                        modifier = Modifier.padding(padding),
                        selectedMapUuid = state.form.mapUuid,
                        mapCardStates = state.mapCardStates,
                        enableContinueButton = state.canContinueFromMap,
                        onAction = onAction
                    )
                }
                ActivityAddFlowStep.ScoreStep -> {
                    ScoreStep(
                        modifier = Modifier.padding(padding),
                        scoreA = state.form.scoreA,
                        scoreB = state.form.scoreB,
                        surrender = state.form.endReason,
                        scoreAError = state.form.scoreAError?.let { stringResource(it) },
                        scoreBError = state.form.scoreBError?.let { stringResource(it) },
                        isPlacementScoreType = state.isPlacementScoreType,
                        enableContinueButton = state.canContinueFromScore,
                        onAction = onAction
                    )
                }
                ActivityAddFlowStep.RankStep -> {
                    RankStep(
                        modifier = Modifier.padding(padding),
                        currentRank = state.currentRank,
                        rankPlacementState = RankPlacementState(
                            rankCardStates = state.rankCardStates,
                            selectedRankTier = state.form.selectedRankTier,
                            placement = state.form.rankPlacement,
                            placementRr = state.form.placementRr,
                            placementRrError = state.form.placementRrError?.let { stringResource(it) },
                        ),
                        rankChangeState = RankChangeState(
                            rrDelta = state.rrDelta,
                            showRankModifier = state.showRankModifier,
                            visibleRrDelta = state.form.visibleRrDelta,
                            matchOutcome = state.matchOutcome,
                            rrDeltaError = state.form.rrDeltaError?.let { stringResource(it) },
                        ),
                        enableContinueButton = state.canContinueFromRank,
                        onAction = onAction,
                    )
                }
                ActivityAddFlowStep.XpStep -> {
                    XpStep(
                        modifier = Modifier.padding(padding),
                        xp = state.form.xp,
                        xpError = state.form.xpError?.let { stringResource(it) },
                        time = state.form.time,
                        dateTimePickerVisible = state.dateTimePickerVisible,
                        enableContinueButton = state.canContinueFromXp,
                        onAction = onAction
                    )
                }
                ActivityAddFlowStep.XpCorrectionStep -> {
                    XpCorrectionStep (
                        modifier = Modifier.padding(padding),
                        xp = state.form.xp,
                        xpError = state.form.xpError?.let { stringResource(it) },
                        enableContinueButton = state.canContinueFromXpCorrection,
                        onAction = onAction
                    )
                }
                ActivityAddFlowStep.RrRefundStep -> {
                    RrRefundStep (
                        modifier = Modifier.padding(padding),
                        visibleRrDelta = state.form.visibleRrDelta,
                        rrDeltaError = state.form.rrDeltaError?.let { stringResource(it) },
                        enableContinueButton = state.canContinueFromRrRefund,
                        onAction = onAction
                    )
                }
            }
        }
    )
}
