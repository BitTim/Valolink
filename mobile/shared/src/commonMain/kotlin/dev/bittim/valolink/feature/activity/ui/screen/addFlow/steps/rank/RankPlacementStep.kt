/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       RankPlacementStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   01.08.26, 12:23
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.ConnectedButtonGroupEntry
import dev.bittim.valolink.core.ui.components.SeamlessLazyColumn
import dev.bittim.valolink.core.ui.components.SingleConnectedButtonGroup
import dev.bittim.valolink.feature.activity.ui.components.rank.RankCard
import dev.bittim.valolink.feature.activity.ui.components.rank.RankCardState
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowAction
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.activity_add_flow_rank_step_no_placement_label
import valolink.shared.generated.resources.activity_add_flow_rank_step_placement_label

@Composable
fun RankPlacementStep(
    modifier: Modifier = Modifier,
    rankCardStates: List<RankCardState>? = null,
    selectedRankTier: Int?,
    placement: Boolean,
    onAction: (ActivityAddFlowAction) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.m)
    ) {
        SingleConnectedButtonGroup(
            initialSelection = if(placement) 1 else 0,
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
            visible = placement
        ) {
            SeamlessLazyColumn {
                items(rankCardStates ?: emptyList()) { rankCardState ->
                    RankCard(
                        modifier = Modifier.fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .border(
                                width = Spacing.xxs,
                                color = if (rankCardState.tier == selectedRankTier) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = MaterialTheme.shapes.medium
                            )
                            .clickable { onAction(ActivityAddFlowAction.RankSelected(rankCardState.tier)) },
                        state = rankCardState
                    )
                }
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
                placement = false,
                onAction = {}
            )
        }
    }
}