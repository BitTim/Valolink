/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowScreen.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   01.08.26, 13:02
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow

import androidx.compose.foundation.layout.*
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.flowScaffold.FlowScaffold
import dev.bittim.valolink.feature.activity.ui.components.match.MatchCard
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.state.*
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps.MapStep
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps.ModeStep
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps.ScoreStep
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps.XpStep
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps.rank.RankChangeState
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps.rank.RankStep
import org.jetbrains.compose.resources.stringResource

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
        menuContent = {
            DropdownMenuItem(
                leadingIcon = {},
                text = {},
                onClick = {}
            )
        },
        hero = {
            MatchCard(
                modifier = Modifier.fillMaxWidth(),
                state = state.matchCardState
            )
        },
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
                        surrender = state.form.surrender,
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
                        enableContinueButton = false,
                        onAction = onAction
                    )
                }
                ActivityAddFlowStep.XpCorrectionStep -> {

                }
                ActivityAddFlowStep.RrRefundStep -> {

                }
            }
        }
    )
}
