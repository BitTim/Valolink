/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       LabeledSlider.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   20.04.25, 03:29
 */

package dev.bittim.valolink.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.annotations.ComponentPreviewAnnotations
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabeledSlider(
    modifier: Modifier = Modifier,
    label: String,
    initialValue: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    isStepped: Boolean,
    showLimitLabels: Boolean,
    onValueChange: (Float) -> Unit
) {
    var value by remember { mutableFloatStateOf(initialValue) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
    ) {
        Text(text = label, style = MaterialTheme.typography.labelLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.m)
        ) {
            if (showLimitLabels) {
                Text(
                    text = valueRange.start.roundToInt().toString(),
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Slider(
                modifier = Modifier.weight(1f),
                value = value,
                valueRange = valueRange,
                steps = if (!isStepped) 0 else valueRange.endInclusive.toInt() - valueRange.start.toInt() - 1,
                onValueChange = {
                    value = it
                    onValueChange(it)
                },
            )

            if (showLimitLabels) {
                Text(
                    text = valueRange.endInclusive.roundToInt().toString(),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@ComponentPreviewAnnotations
@Composable
fun LabeledSliderPreview() {
    ValolinkTheme {
        Surface {
            Column {
                LabeledSlider(
                    label = "This is a test slider",
                    initialValue = 5f,
                    valueRange = 0f..10f,
                    isStepped = true,
                    showLimitLabels = false,
                    onValueChange = {}
                )

                Spacer(modifier = Modifier.height(Spacing.m))

                LabeledSlider(
                    label = "This is another test slider",
                    initialValue = 214f,
                    valueRange = 0f..1000f,
                    isStepped = false,
                    showLimitLabels = true,
                    onValueChange = {}
                )
            }
        }
    }
}
