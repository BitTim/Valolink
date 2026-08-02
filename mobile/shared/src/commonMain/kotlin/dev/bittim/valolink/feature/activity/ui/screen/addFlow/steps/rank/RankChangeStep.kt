/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       RankChangeStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   31.07.26, 12:12
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps.rank

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.domain.model.MatchOutcome
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.OutlinedTextFieldWithError
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowAction
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.*
import kotlin.math.absoluteValue

data class RankChangeState(
    val rrDelta: Int? = null,
    val visibleRrDelta: Int? = null,
    val matchOutcome: MatchOutcome? = null,
    val rrDeltaError: String? = null,
    val showRankModifier: Boolean = false,
    val maxRrDigits: Int = 2
)

/**
 * Renders the rank rating change input and optional rank modifier selector.
 *
 * @param modifier The modifier applied to the component.
 * @param state The current rank change values, validation state, and display configuration.
 * @param onAction Receives actions when the rank rating value or sign changes.
 */
@Composable
fun RankChangeStep(
    modifier: Modifier = Modifier,
    state: RankChangeState,
    onAction: (ActivityAddFlowAction) -> Unit,
) {
    var rawRrDelta by rememberSaveable(state.visibleRrDelta) { mutableStateOf(state.visibleRrDelta?.absoluteValue?.toString() ?: "") }
    var signChecked by rememberSaveable(state.visibleRrDelta, state.matchOutcome) {
        mutableStateOf(state.visibleRrDelta?.let { it < 0 } ?: (state.matchOutcome == MatchOutcome.Loss))
    }
    var modifierChecked by rememberSaveable(state.rrDelta) { mutableStateOf(false) }

    val action = {
        val sign = if (signChecked) "-" else ""
        onAction(ActivityAddFlowAction.RrDeltaChanged("$sign$rawRrDelta"))
    }

    Column(
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.s),
        ) {
            OutlinedTextFieldWithError(
                value = rawRrDelta,
                onValueChange = {
                    rawRrDelta = it.take(state.maxRrDigits)
                    action()
                },
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.activity_add_flow_rank_step_rr_label),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                error = state.rrDeltaError,
                leadingIcon = {
                    IconToggleButton(
                        checked = signChecked,
                        onCheckedChange = {
                            signChecked = it
                            action()
                        }
                    ) {
                        AnimatedContent(
                            targetState = signChecked
                        ) {
                            Icon(
                                imageVector = if (it) Icons.Default.Remove else Icons.Default.Add,
                                contentDescription = when (it) {
                                    false -> stringResource(Res.string.activity_add_flow_rank_step_rr_sign_positive_desc)
                                    true -> stringResource(Res.string.activity_add_flow_rank_step_rr_sign_negative_desc)
                                }
                            )
                        }
                    }
                },
            )
        }

        AnimatedVisibility(
            visible = state.showRankModifier
        ) {
            AnimatedContent(
                targetState = state.rrDelta!!
            ) { rrDelta ->
                when {
                    rrDelta < 0 -> FilterChip(
                        selected = modifierChecked,
                        onClick = { modifierChecked = !modifierChecked },
                        label = { Text(stringResource(Res.string.activity_add_flow_rank_step_modifier_label_rank_shield)) },
                        leadingIcon = { Icon(imageVector = Icons.Default.Shield, contentDescription = null) }
                    )

                    rrDelta > 0 -> FilterChip(
                        selected = modifierChecked,
                        onClick = { modifierChecked = !modifierChecked},
                        label = { Text(stringResource(Res.string.activity_add_flow_rank_step_modifier_label_double_rank_up)) },
                        leadingIcon = { Icon(imageVector = Icons.Default.KeyboardDoubleArrowUp, contentDescription = null) }
                    )
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
                    rrDelta = 50,
                    visibleRrDelta = 99,
                    rrDeltaError = null,
                    showRankModifier = true,
                ),
                onAction = { }
            )
        }
    }
}