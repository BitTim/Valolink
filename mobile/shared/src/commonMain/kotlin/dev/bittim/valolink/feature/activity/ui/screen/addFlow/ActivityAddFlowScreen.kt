/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowScreen.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   09.06.26, 21:27
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow

import androidx.compose.foundation.layout.*
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.domain.model.ValoMapCategory
import dev.bittim.valolink.core.domain.model.ValoModeCategory
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.flowScaffold.FlowScaffold
import dev.bittim.valolink.feature.activity.ui.components.match.MatchCard
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps.MapStep
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps.ModeStep

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
                        selectedModeUuid = state.selectedModeUuid,
                        modes = state.modes,
                        onAction = onAction
                    )
                }
                ActivityAddFlowStep.MapStep -> {
                    val currentModeCategory = remember(state) { state.modes?.find { it.uuid == state.selectedModeUuid }?.category }

                    MapStep(
                        modifier = Modifier.padding(padding),
                        selectedMapUuid = state.selectedMapUuid,
                        maps = remember(currentModeCategory, state) { state.maps?.filter {
                            when(currentModeCategory) {
                                ValoModeCategory.Tutorial -> it.category == ValoMapCategory.Tutorial
                                ValoModeCategory.Range -> it.category == ValoMapCategory.Range
                                ValoModeCategory.Standard -> it.category == ValoMapCategory.Standard
                                ValoModeCategory.Deathmatch -> it.category == ValoMapCategory.Standard
                                ValoModeCategory.TDM -> it.category == ValoMapCategory.TDM
                                ValoModeCategory.Skirmish -> it.category == ValoMapCategory.Skirmish
                                else -> false
                            }
                        }},
                        onAction = onAction
                    )
                }
                ActivityAddFlowStep.DetailsStep -> {

                }
                ActivityAddFlowStep.XpCorrectionStep -> {

                }
                ActivityAddFlowStep.RrRefundStep -> {

                }
            }
        }
    )
}