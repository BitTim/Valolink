/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       DateTimePickerDialog.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   17.06.26, 04:10
 */

package dev.bittim.valolink.feature.activity.ui.components.dateTimePicker

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

@Composable
fun DateTimePickerDialog(
    initialTime: Instant?,
    onDismiss: () -> Unit,
    onDateTimeSelected: (dateMillis: Long, hour: Int, minute: Int) -> Unit
) {
    val timeZone = TimeZone.currentSystemDefault()
    val initialLocalTime = initialTime?.toLocalDateTime(timeZone)

    var dateMillis by rememberSaveable { mutableStateOf(initialLocalTime?.date?.atStartOfDayIn(timeZone)?.toEpochMilliseconds() ?: 0L) }
    var hour by rememberSaveable { mutableStateOf(initialLocalTime?.hour ?: 0) }
    var minute by rememberSaveable { mutableStateOf(initialLocalTime?.minute ?: 0) }

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
                        dateMillis = dateMillis,
                        onDismiss = onDismiss,
                        onDateSelected = { newDateMillis ->
                            dateMillis = newDateMillis
                            isSecondStep = true
                        }
                    )
                } else {
                    TimePickerContent(
                        initialHour = hour,
                        initialMinute = minute,
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