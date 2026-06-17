/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       XpStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   17.06.26, 04:23
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.OutlinedTextFieldWithError
import dev.bittim.valolink.feature.activity.ui.components.dateTimePicker.DateTimePickerDialog
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowAction
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.*
import kotlin.time.Clock
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun XpStep(
    modifier: Modifier = Modifier,
    xp: Int?,
    xpError: String?,
    time: Instant,
    dateTimePickerVisible: Boolean,
    enableContinueButton: Boolean,
    onAction: (ActivityAddFlowAction) -> Unit
) {
    var rawXp by rememberSaveable { mutableStateOf(xp?.toString() ?: "") }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing.l)
    ) {
        Text(
            text = stringResource(Res.string.activity_add_flow_xp_step_title),
            style = MaterialTheme.typography.titleLarge
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.m)
        ) {
            OutlinedTextFieldWithError(
                value = rawXp,
                onValueChange = {
                    rawXp = it
                    onAction(ActivityAddFlowAction.XpChanged(rawXp))
                },
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.activity_add_flow_xp_step_xp_label),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                error = xpError,
            )
        }

        Column(
            modifier = Modifier.padding(top = Spacing.m)
        ) {
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onAction(ActivityAddFlowAction.ChangeTime) }
            ) {
                Text(text = stringResource(Res.string.activity_add_flow_xp_step_change_time))
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = enableContinueButton,
                onClick = { onAction(ActivityAddFlowAction.XpFinish) }
            ) {
                Text(text = stringResource(Res.string.generic_button_finish))
            }
        }
    }

    if (dateTimePickerVisible) {
        DateTimePickerDialog(
            initialTime = time,
            onDismiss = { onAction(ActivityAddFlowAction.DateTimePickerDismiss) },
            onDateTimeSelected = { dateMillis, hour, minute -> onAction(ActivityAddFlowAction.DateTimeSelected(dateMillis, hour, minute)) }
        )
    }
}

@Composable
@Preview
fun XpStepPreview() {
    MaterialTheme {
        Surface {
            XpStep(
                modifier = Modifier,
                xp = null,
                xpError = null,
                enableContinueButton = true,
                time = Clock.System.now(),
                dateTimePickerVisible = false,
                onAction = {}
            )
        }
    }
}