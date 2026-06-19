/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       DateTimePickerDialog.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.06.26, 02:25
 */

package dev.bittim.valolink.feature.activity.ui.components.dateTimePicker

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.datetime.*
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.date_time_picker_error_out_of_range
import kotlin.time.Clock
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerDialog(
    initialTime: Instant?,
    allowFutureTime: Boolean = false,
    onDismiss: () -> Unit,
    onDateTimeSelected: (dateMillis: Long, hour: Int, minute: Int) -> Unit
) {
    val timeZone = TimeZone.currentSystemDefault()
    val initialLocalTime = initialTime?.toLocalDateTime(timeZone)

    var dateMillis by rememberSaveable { mutableStateOf(initialLocalTime?.date?.atStartOfDayIn(TimeZone.UTC)?.toEpochMilliseconds() ?: 0L) }
    var hour by rememberSaveable { mutableStateOf(initialLocalTime?.hour ?: 0) }
    var minute by rememberSaveable { mutableStateOf(initialLocalTime?.minute ?: 0) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = dateMillis,
        selectableDates = if (!allowFutureTime) ValoSelectableDates else DatePickerDefaults.AllDates
    )

    val timePickerState = rememberTimePickerState(
        initialHour = hour,
        initialMinute = minute
    )

    val ep1Start = Instant.fromEpochMilliseconds(ValoSelectableDates.EP1_START_TIME)
    val isOutOfRange by remember(allowFutureTime) {
        derivedStateOf {
            if (allowFutureTime) return@derivedStateOf false
            val selected = Instant.fromEpochMilliseconds(dateMillis)
                .toLocalDateTime(TimeZone.UTC).date
                .atTime(timePickerState.hour, timePickerState.minute)
                .toInstant(timeZone)
            selected !in ep1Start..Clock.System.now()
        }
    }

    var isSecondStep by rememberSaveable { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier =
                Modifier.widthIn(max = 360.dp)
                    .background(
                        shape = MaterialTheme.shapes.extraLarge,
                        color = MaterialTheme.colorScheme.surface
                    ),
        ) {
            AnimatedContent(
                targetState = isSecondStep,
                transitionSpec = {
                    ContentTransform(
                        targetContentEnter = fadeIn(),
                        initialContentExit = fadeOut(),
                        sizeTransform = SizeTransform()
                    )
                }
            ) {
                if(!it) {
                    DatePickerContent(
                        datePickerState = datePickerState,
                        onDismiss = onDismiss,
                        onDateSelected = { newDateMillis ->
                            dateMillis = newDateMillis
                            isSecondStep = true
                        }
                    )
                } else {
                    TimePickerContent(
                        timePickerState = timePickerState,
                        enableFinish = !isOutOfRange,
                        error = if (isOutOfRange) stringResource(Res.string.date_time_picker_error_out_of_range) else null,
                        onDismiss = { isSecondStep = false },
                        onTimeSelected = { newHour, newMinute ->
                            onDateTimeSelected(dateMillis, newHour, newMinute)
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}