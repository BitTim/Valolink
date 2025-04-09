/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       LabeledSwitch.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.04.25, 15:50
 */

package dev.bittim.valolink.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.annotations.ComponentPreviewAnnotations

@Composable
fun LabeledSwitch(
    modifier: Modifier = Modifier,
    label: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    showTooltip: Boolean = false,
    onTooltip: () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge
            )

            if (showTooltip) {
                IconButton(
                    onClick = onTooltip
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.HelpOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Switch(
            checked = value,
            onCheckedChange = onValueChange
        )
    }
}

@ComponentPreviewAnnotations
@Composable
fun LabeledSwitchPreview() {
    ValolinkTheme {
        Surface {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.m)
            ) {
                LabeledSwitch(label = "This is a test setting", value = true, onValueChange = {})
                LabeledSwitch(
                    label = "This is another test setting with a tooltip",
                    value = false,
                    onValueChange = {},
                    showTooltip = true
                )
            }
        }
    }
}
