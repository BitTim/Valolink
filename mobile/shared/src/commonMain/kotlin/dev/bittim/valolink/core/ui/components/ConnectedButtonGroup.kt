/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ConnectedButtonGroup.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   15.06.26, 20:16
 */

package dev.bittim.valolink.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

data class ConnectedButtonGroupEntry(
    val label: String,
    val icon: (@Composable () -> Unit)?,
    val weight: Float = Float.NaN
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SingleConnectedButtonGroup(
    modifier: Modifier = Modifier,
    entries: List<ConnectedButtonGroupEntry>,
    horizontalArrangement: Arrangement.Horizontal = ButtonGroupDefaults.HorizontalArrangement,
    onSelectionChange: (index: Int) -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    ButtonGroup(
        modifier = modifier,
        overflowIndicator = { },
        horizontalArrangement = horizontalArrangement
    ) {
        entries.forEachIndexed { index, entry ->
            val checked = index == selectedIndex

            this.toggleableItem(
                checked = checked,
                label = entry.label,
                onCheckedChange = {
                    selectedIndex = index
                    onSelectionChange(index)
                },
                icon = entry.icon,
                weight = entry.weight
            )
        }
    }
}