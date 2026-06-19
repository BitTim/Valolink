/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       DatePickerContent.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.06.26, 01:20
 */

package dev.bittim.valolink.feature.activity.ui.components.dateTimePicker

import androidx.compose.foundation.layout.*
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.generic_button_cancel
import valolink.shared.generated.resources.generic_button_continue

@Composable
fun DatePickerContent(
    datePickerState: DatePickerState,
    onDismiss: () -> Unit,
    onDateSelected: (dateMillis: Long) -> Unit,
) {
    Column(
        modifier = Modifier.padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DatePicker(
            modifier = Modifier.fillMaxWidth(),
            state = datePickerState
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp, start = 24.dp, end = 24.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onDismiss) { Text(stringResource(Res.string.generic_button_cancel)) }
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis ?: 0L)
                }
            ) { Text(stringResource(Res.string.generic_button_continue)) }
        }
    }
}