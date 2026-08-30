/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       RankChangeStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.08.26, 03:05
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps.rank

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.domain.model.MatchOutcome
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowAction
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.sections.RrSection
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.activity_add_flow_rank_step_modifier_label_double_rank_up
import valolink.shared.generated.resources.activity_add_flow_rank_step_modifier_label_rank_shield

data class RankChangeState(
    val rankStepContext: RankStepContext = RankStepContext(),
    val rrDelta: Int? = null,
    val showRankModifier: Boolean = false,
    val visibleRrDelta: Int? = null,
    val matchOutcome: MatchOutcome? = null,
    val rrDeltaError: String? = null,
)

enum class RankModifier {
    NONE,
    RANK_SHIELD,
    DOUBLE_RANK_UP
}

/**
 * Renders rank-rating input and an optional rank modifier selector.
 *
 * @param state The rank-change values, validation state, and display configuration.
 * @param onAction Receives actions from rank-rating input changes.
 */
@Composable
fun RankChangeStep(
    modifier: Modifier = Modifier,
    state: RankChangeState,
    onAction: (ActivityAddFlowAction) -> Unit,
) {
    var modifierChecked by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier,
    ) {
        RrSection(
            modifier = Modifier.weight(1f),
            visibleRrDelta = state.visibleRrDelta,
            maxRrDigits = state.rankStepContext.maxRrDigits,
            rrDeltaError = state.rrDeltaError,
            matchOutcome = state.matchOutcome,
            onAction = onAction
        )

        AnimatedVisibility(
            visible = state.showRankModifier || modifierChecked
        ) {
            AnimatedContent(
                targetState = when {
                    state.rrDelta!! > 0 -> RankModifier.DOUBLE_RANK_UP
                    state.rrDelta < 0 -> RankModifier.RANK_SHIELD
                    state.rrDelta <= 0 && modifierChecked -> RankModifier.RANK_SHIELD
                    else -> RankModifier.NONE
                }
            ) { modifier ->
                when(modifier) {
                    RankModifier.RANK_SHIELD -> FilterChip(
                        selected = modifierChecked,
                        onClick = {
                            modifierChecked = !modifierChecked
                            onAction(ActivityAddFlowAction.RankModifierChanged(modifierChecked))
                        },
                        label = { Text(stringResource(Res.string.activity_add_flow_rank_step_modifier_label_rank_shield)) },
                        leadingIcon = { Icon(imageVector = Icons.Default.Shield, contentDescription = null) }
                    )

                    RankModifier.DOUBLE_RANK_UP -> FilterChip(
                        selected = modifierChecked,
                        onClick = {
                            modifierChecked = !modifierChecked
                            onAction(ActivityAddFlowAction.RankModifierChanged(modifierChecked))
                        },
                        label = { Text(stringResource(Res.string.activity_add_flow_rank_step_modifier_label_double_rank_up)) },
                        leadingIcon = { Icon(imageVector = Icons.Default.KeyboardDoubleArrowUp, contentDescription = null) }
                    )

                    RankModifier.NONE -> {}
                }
            }
        }
    }
}

@Preview
@Composable
private fun RankChangeStepPreview() {
    MaterialTheme {
        Surface {
            RankChangeStep(
                modifier = Modifier.fillMaxSize(),
                state = RankChangeState(
                    visibleRrDelta = 99,
                    rrDeltaError = null,
                    rrDelta = 50,
                    showRankModifier = true,
                ),
                onAction = { }
            )
        }
    }
}