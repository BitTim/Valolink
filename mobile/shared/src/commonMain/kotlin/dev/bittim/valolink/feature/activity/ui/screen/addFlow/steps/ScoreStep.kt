/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ScoreStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   15.06.26, 20:19
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.domain.model.MatchEndReason
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.ConnectedButtonGroupEntry
import dev.bittim.valolink.core.ui.components.OutlinedTextFieldWithError
import dev.bittim.valolink.core.ui.components.SingleConnectedButtonGroup
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowAction
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.*

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ScoreStep(
    modifier: Modifier = Modifier,
    scoreA: Int?,
    scoreB: Int?,
    scoreAError: String?,
    scoreBError: String?,
    isPlacementScoreType: Boolean,
    onAction: (ActivityAddFlowAction) -> Unit
) {
    var rawScoreA by rememberSaveable { mutableStateOf(scoreA?.toString() ?: "") }
    var rawScoreB by rememberSaveable { mutableStateOf(scoreB?.toString() ?: "") }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing.l)
    ) {
        Text(
            text = stringResource(Res.string.activity_add_flow_score_step_title),
            style = MaterialTheme.typography.titleLarge
        )

        AnimatedContent(
            targetState = isPlacementScoreType
        ) { isPlacementScoreType ->
            if(isPlacementScoreType) {
                OutlinedTextFieldWithError(
                    value = rawScoreA,
                    onValueChange = {
                        rawScoreA = it
                        onAction(ActivityAddFlowAction.ScoreAChanged(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(Res.string.activity_add_flow_score_step_place_label),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    error = scoreAError,
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(Spacing.m)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.m)
                    ) {
                        OutlinedTextFieldWithError(
                            value = rawScoreA,
                            onValueChange = {
                                rawScoreA = it
                                onAction(ActivityAddFlowAction.ScoreAChanged(it))
                            },
                            modifier = Modifier.weight(1f),
                            label = stringResource(Res.string.activity_add_flow_score_step_you_label),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            error = scoreAError
                        )

                        OutlinedTextFieldWithError(
                            value = rawScoreB,
                            onValueChange = {
                                rawScoreB = it
                                onAction(ActivityAddFlowAction.ScoreBChanged(it))
                            },
                            modifier = Modifier.weight(1f),
                            label = stringResource(Res.string.activity_add_flow_score_step_enemy_label),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            error = scoreBError
                        )
                    }

                    Column {
                        Text(
                            text = stringResource(Res.string.activity_add_flow_score_step_surrendered_by_label),
                            style = MaterialTheme.typography.labelMedium
                        )

                        SingleConnectedButtonGroup(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                            entries = MatchEndReason.entries.map {
                                ConnectedButtonGroupEntry(
                                    label = when(it) {
                                        MatchEndReason.COMPLETED -> stringResource(Res.string.activity_add_flow_score_step_surrendered_by_no_one_label)
                                        MatchEndReason.SURRENDER_A -> stringResource(Res.string.activity_add_flow_score_step_surrendered_by_you_label)
                                        MatchEndReason.SURRENDER_B -> stringResource(Res.string.activity_add_flow_score_step_surrendered_by_enemy_label)
                                    },
                                    icon = null,
                                    weight = 1f
                                )
                            },
                            onSelectionChange = { onAction(ActivityAddFlowAction.SurrenderChanged(MatchEndReason.entries[it])) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun ScoreStepPreview() {
    MaterialTheme {
        Surface {
            ScoreStep(
                modifier = Modifier.fillMaxSize(),
                scoreA = null,
                scoreB = null,
                scoreAError = null,
                scoreBError = "Sample Error",
                isPlacementScoreType = false,
                onAction = {}
            )
        }
    }
}