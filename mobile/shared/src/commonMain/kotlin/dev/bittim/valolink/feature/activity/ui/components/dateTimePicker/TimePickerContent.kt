/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       TimePickerContent.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.06.26, 02:25
 */

package dev.bittim.valolink.feature.activity.ui.components.dateTimePicker

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerContent(
    timePickerState: TimePickerState,
    enableFinish: Boolean,
    error: String?,
    onDismiss: () -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
) {
    var showDial by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.padding(24.dp)
            .width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            text = stringResource(Res.string.dialog_time_picker_title),
            style = MaterialTheme.typography.labelLarge
        )

        AnimatedContent (
            targetState = showDial
        ) {
            if (it) {
                TimePicker(
                    state = timePickerState,
                )
            } else {
                TimeInput(
                    state = timePickerState,
                )
            }
        }

        AnimatedVisibility(
            visible = error != null
        ) {
            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 20.dp),
                text = error ?: "",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        Row(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
        ) {
            IconButton(onClick = { showDial = !showDial }) {
                AnimatedContent(
                    targetState = showDial
                ) {
                    if (it) {
                        Icon(
                            imageVector = Icons.Default.Keyboard,
                            contentDescription = stringResource(Res.string.iconcd_time_picker_toggle_keyboard),
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = stringResource(Res.string.iconcd_time_picker_toggle_dial),
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = onDismiss) { Text(stringResource(Res.string.generic_button_back)) }
            TextButton(
                enabled = enableFinish,
                onClick = {
                    onTimeSelected(timePickerState.hour, timePickerState.minute)
                }
            ) { Text(stringResource(Res.string.generic_button_finish)) }
        }
    }
}