/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       RankStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.06.26, 20:35
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.OutlinedTextFieldWithError
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowAction
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.activity_add_flow_rank_step_rr_label
import valolink.shared.generated.resources.activity_add_flow_rank_step_title
import valolink.shared.generated.resources.generic_button_continue

@Composable
fun RankStep(
    modifier: Modifier = Modifier,
    hasRankPlacement: Boolean,
    rr: Int?,
    rrError: String?,
    enableContinueButton: Boolean,
    onAction: (ActivityAddFlowAction) -> Unit
) {
    var rawRr by rememberSaveable { mutableStateOf(rr?.toString() ?: "") }

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
                        horizontalArrangement = Arrangement.spacedBy(Spacing.m),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextFieldWithError(
                            value = rawRr,
                            onValueChange = {
                                rawRr = it
                                onAction(ActivityAddFlowAction.RrChanged(rawRr))
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
                                imageVector = Icons.Default.Check,
                                contentDescription = null
                            )
                        }
                    }
                } else {

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