/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       XpStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.08.26, 17:17
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.domain.model.ActivityType
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.feature.activity.ui.components.dateTimePicker.DateTimePickerDialog
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowAction
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.sections.XpSection
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.activity_add_flow_xp_step_change_time
import valolink.shared.generated.resources.activity_add_flow_xp_step_title
import valolink.shared.generated.resources.generic_button_finish
import kotlin.time.Clock
import kotlin.time.Instant

/**
 * Displays the XP entry step with time controls and an optional date-time picker.
 *
 * @param xp The current XP value.
 * @param xpError The validation error for the XP value, if any.
 * @param time The initial date and time shown by the date-time picker.
 * @param dateTimePickerVisible Whether to display the date-time picker.
 * @param enableContinueButton Whether the finish button is enabled.
 * @param onAction Handles user actions from the step.
 */
/**
 * Displays the XP entry step, including XP input, time selection, and completion controls.
 *
 * @param xp The current XP value.
 * @param xpError The validation error associated with the XP value, if any.
 * @param time The initial date and time shown by the picker.
 * @param dateTimePickerVisible Whether the date and time picker is displayed.
 * @param enableContinueButton Whether the finish button is enabled.
 * @param onAction Handles actions from the XP entry screen.
 */
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
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing.l)
    ) {
        Text(
            text = stringResource(Res.string.activity_add_flow_xp_step_title),
            style = MaterialTheme.typography.titleLarge
        )

        XpSection(
            modifier = Modifier.weight(1f),
            xp = xp,
            xpError = xpError,
            onAction = onAction
        )

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
                onClick = { onAction(ActivityAddFlowAction.Finish(ActivityType.MATCH)) }
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