/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       RankPlacementStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.08.26, 03:40
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps.rank

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.ConnectedButtonGroupEntry
import dev.bittim.valolink.core.ui.components.OutlinedTextFieldWithError
import dev.bittim.valolink.core.ui.components.SeamlessLazyColumn
import dev.bittim.valolink.core.ui.components.SingleConnectedButtonGroup
import dev.bittim.valolink.feature.activity.ui.components.rank.RankCard
import dev.bittim.valolink.feature.activity.ui.components.rank.RankCardState
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowAction
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.activity_add_flow_rank_step_no_placement_label
import valolink.shared.generated.resources.activity_add_flow_rank_step_placement_label
import valolink.shared.generated.resources.activity_add_flow_rank_step_rr_label

data class RankPlacementState (
    val rankStepContext: RankStepContext = RankStepContext(),
    val rankCardStates: List<RankCardState>? = null,
    val selectedRankTier: Int?,
    val placementRr: Int? = null,
    val placementRrError: String? = null,
    val placement: Boolean
)

/**
 * Displays controls for enabling rank placement and selecting a rank.
 *
 * @param state The state of the rank placement step.
 * @param onAction Handles rank placement and rank selection changes.
 */
@Composable
fun RankPlacementStep(
    modifier: Modifier = Modifier,
    state: RankPlacementState,
    onAction: (ActivityAddFlowAction) -> Unit,
) {
    var rawPlacementRr by rememberSaveable(state.placementRr) { mutableStateOf(state.placementRr?.toString() ?: "") }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.m)
    ) {
        SingleConnectedButtonGroup(
            initialSelection = if(state.placement) 1 else 0,
            entries = listOf(
                ConnectedButtonGroupEntry(
                    label = stringResource(Res.string.activity_add_flow_rank_step_no_placement_label),
                    icon = null,
                    weight = 1f
                ),
                ConnectedButtonGroupEntry(
                    label = stringResource(Res.string.activity_add_flow_rank_step_placement_label),
                    icon = null,
                    weight = 1f
                )
            ),
            onSelectionChange = { onAction(ActivityAddFlowAction.RankPlacementChanged(it != 0)) }
        )

        AnimatedVisibility(
            modifier = Modifier.weight(1f),
            visible = state.placement
        ) {
            Column {
                SeamlessLazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(state.rankCardStates ?: emptyList()) { rankCardState ->
                        RankCard(
                            modifier = Modifier.fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium)
                                .border(
                                    width = Spacing.xxs,
                                    color = if (rankCardState.tier == state.selectedRankTier) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .clickable { onAction(ActivityAddFlowAction.RankSelected(rankCardState.tier)) },
                            state = rankCardState
                        )
                    }
                }

                OutlinedTextFieldWithError(
                    value = rawPlacementRr,
                    onValueChange = {
                        rawPlacementRr = it.take(state.rankStepContext.maxRrDigits)
                        onAction(ActivityAddFlowAction.PlacementRrChanged(rawPlacementRr))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(Res.string.activity_add_flow_rank_step_rr_label),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    error = state.placementRrError,
                )
            }
        }
    }
}

@Preview
@Composable
private fun RankPlacementStepPreview() {
    MaterialTheme {
        Surface {
            RankPlacementStep(
                modifier = Modifier.fillMaxSize(),
                state = RankPlacementState(
                    placement = true,
                    rankCardStates = listOf(
                        RankCardState(
                            tier = 3,
                            name = "Rank Name",
                            division = "Rank Division",
                            imageUrl = null,
                            color = Color.Blue,
                            backgroundColor = Color.Cyan
                        )
                    ),
                    selectedRankTier = null,
                ),
                onAction = {}
            )
        }
    }
}