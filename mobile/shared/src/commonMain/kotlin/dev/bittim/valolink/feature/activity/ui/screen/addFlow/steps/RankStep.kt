/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       RankStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   28.06.26, 13:07
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.domain.model.MatchOutcome
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.OutlinedTextFieldWithError
import dev.bittim.valolink.feature.activity.ui.components.rank.RankCardState
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowAction
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.*
import kotlin.math.absoluteValue

@Composable
fun RankStep(
    modifier: Modifier = Modifier,
    rankCardStates: List<RankCardState>? = null,
    hasRankPlacement: Boolean,
    rr: Int?,
    rrError: String?,
    matchOutcome: MatchOutcome? = null,
    enableContinueButton: Boolean,
    onAction: (ActivityAddFlowAction) -> Unit,
    maxRrDigits: Int = 2
) {
    var rawRr by rememberSaveable { mutableStateOf(rr?.absoluteValue?.toString() ?: "") }
    var signChecked by rememberSaveable { mutableStateOf(if(rr == null) matchOutcome == MatchOutcome.Loss else rr < 0) }

    val action = {
        val sign = if (signChecked) "-" else ""
        onAction(ActivityAddFlowAction.RrChanged("$sign$rawRr"))
    }

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
                targetState = hasRankPlacement
            ) { hasRankPlacement ->
                if (hasRankPlacement) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilledTonalIconToggleButton(
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

                        OutlinedTextFieldWithError(
                            value = rawRr,
                            onValueChange = {
                                rawRr = it.take(maxRrDigits)
                                action()
                            },
                            modifier = Modifier.weight(1f),
                            label = stringResource(Res.string.activity_add_flow_rank_step_rr_label),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            error = rrError,
                        )

                        FilledTonalIconToggleButton(
                            checked = false,
                            onCheckedChange = {}
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null
                            )
                        }
                    }
                } else {
                    Text("Placement")
                }
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = enableContinueButton,
            onClick = {  }
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
            RankStep(
                hasRankPlacement = true,
                rr = null,
                rrError = null,
                enableContinueButton = true,
                onAction = {  },
            )
        }
    }
}