/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       RankStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.08.26, 03:40
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps.rank

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.domain.model.Rank
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowAction
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.activity_add_flow_rank_step_title
import valolink.shared.generated.resources.generic_button_continue

data class RankStepContext(
    val maxRrDigits: Int = 2
)

/**
 * Displays the rank selection or rank change step in the activity-add flow.
 *
 * @param currentRank The user's current rank, when available.
 * @param rankPlacementState The state used when placing a rank.
 * @param rankChangeState The state used when changing an existing rank.
 * @param enableContinueButton Whether the Continue button is enabled.
 * @param onAction Handles actions emitted by the step.
 */
@Composable
fun RankStep(
    modifier: Modifier = Modifier,
    currentRank: Rank?,
    rankPlacementState: RankPlacementState,
    rankChangeState: RankChangeState?,
    enableContinueButton: Boolean,
    onAction: (ActivityAddFlowAction) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing.l)
    ) {
        Text(
            text = stringResource(Res.string.activity_add_flow_rank_step_title),
            style = MaterialTheme.typography.titleLarge
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.m)
        ) {
            AnimatedContent(
                targetState = currentRank != null && currentRank.rank.tier != 0
            ) { hasRankPlacement ->
                if (hasRankPlacement && rankChangeState != null) {
                    RankChangeStep(
                        modifier = Modifier.weight(1f),
                        state = rankChangeState,
                        onAction = onAction,
                    )
                } else {
                    RankPlacementStep(
                        modifier = Modifier.weight(1f),
                        state = rankPlacementState,
                        onAction = onAction,
                    )
                }
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = enableContinueButton,
            onClick = { onAction(ActivityAddFlowAction.RankContinue) }
        ) {
            Text(text = stringResource(Res.string.generic_button_continue))
        }
    }
}

@Composable
@Preview
fun RankStepPreview() {
    MaterialTheme {
        Surface {
            val rankStepContext = RankStepContext()

            RankStep(
                currentRank = null,
                rankChangeState = RankChangeState(
                    rankStepContext = rankStepContext,
                    rrDelta = null,
                    showRankModifier = false
                ),
                rankPlacementState = RankPlacementState(
                    rankStepContext = rankStepContext,
                    rankCardStates = emptyList(),
                    selectedRankTier = null,
                    placement = false,

                ),
                enableContinueButton = true,
                onAction = {  },
            )
        }
    }
}