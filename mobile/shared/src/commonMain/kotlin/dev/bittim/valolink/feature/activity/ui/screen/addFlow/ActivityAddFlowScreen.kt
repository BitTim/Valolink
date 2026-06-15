/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowScreen.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   13.06.26, 20:29
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
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps.MapStep
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps.ModeStep
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps.ScoreStep
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
                        selectedModeUuid = state.modeUuid,
                        modeCardStates = state.modeCardStates,
                        onAction = onAction
                    )
                }
                ActivityAddFlowStep.MapStep -> {
                    MapStep(
                        modifier = Modifier.padding(padding),
                        selectedMapUuid = state.mapUuid,
                        mapCardStates = state.mapCardStates,
                        onAction = onAction
                    )
                }
                ActivityAddFlowStep.ScoreStep -> {
                    ScoreStep(
                        modifier = Modifier.padding(padding),
                        scoreA = state.scoreA,
                        scoreB = state.scoreB,
                        scoreAError = state.scoreAError?.let { stringResource(it) },
                        scoreBError = state.scoreBError?.let { stringResource(it) },
                        isPlacementScoreType = state.isPlacementScoreType,
                        onAction = onAction
                    )
                }
                ActivityAddFlowStep.OutcomeStep -> {

                }
                ActivityAddFlowStep.XpCorrectionStep -> {

                }
                ActivityAddFlowStep.RrRefundStep -> {

                }
            }
        }
    )
}