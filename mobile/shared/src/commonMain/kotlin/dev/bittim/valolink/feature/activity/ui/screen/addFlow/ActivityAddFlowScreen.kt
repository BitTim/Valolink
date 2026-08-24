/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowScreen.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.08.26, 14:27
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.flowScaffold.FlowScaffold
import dev.bittim.valolink.feature.activity.ui.components.match.MatchCard
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.state.*
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps.*
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps.rank.RankChangeState
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps.rank.RankStep
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.activity_add_flow_rr_refund_menu_item
import valolink.shared.generated.resources.activity_add_flow_xp_correction_menu_item

/**
 * Renders the cancellable multi-step activity-entry flow.
 *
 * @param state The current flow state.
 * @param onAction Handles actions emitted by the flow.
 */
/**
 * Renders the cancellable activity-entry flow and dispatches user actions.
 *
 * @param state The current flow state.
 * @param onAction Callback invoked for flow and form actions.
 */
/**
 * Displays the cancellable activity-entry flow and dispatches user interactions as actions.
 *
 * @param state The current flow state.
 * @param onAction Callback invoked for flow and step interactions.
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
        menuContent = { dismiss ->
            DropdownMenuItem(
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                },
                text = {
                    Text(text = stringResource(Res.string.activity_add_flow_xp_correction_menu_item))
                },
                onClick = {
                    onAction(ActivityAddFlowAction.ToXpCorrection)
                    dismiss()
                }
            )

            DropdownMenuItem(
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Replay, contentDescription = null)
                },
                text = {
                    Text(text = stringResource(Res.string.activity_add_flow_rr_refund_menu_item))
                },
                enabled = state.currentRank != null && state.currentRank.rank.tier != 0 && !state.form.rankPlacement,
                onClick = {
                    onAction(ActivityAddFlowAction.ToRrRefund)
                    dismiss()
                }
            )
        },
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
                        selectedRankTier = state.form.selectedRankTier,
                        placement = state.form.rankPlacement,
                        rankCardStates = state.rankCardStates,
                        rankChangeState = RankChangeState(
                            rrDelta = state.rrDelta,
                            visibleRrDelta = state.form.visibleRrDelta,
                            matchOutcome = state.matchOutcome,
                            rrDeltaError = state.form.rrDeltaError?.let { stringResource(it) },
                            showRankModifier = state.showRankModifier,
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
