/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ConnectedButtonGroup.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   28.06.26, 12:33
 */

package dev.bittim.valolink.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class ButtonGroupStyle {
    Filled,
    Tonal
}

data class ConnectedButtonGroupEntry(
    val label: String,
    val icon: (@Composable () -> Unit)?,
    val weight: Float = Float.NaN,
    val enabled: Boolean = true,
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SingleConnectedButtonGroup(
    modifier: Modifier = Modifier,
    entries: List<ConnectedButtonGroupEntry>,
    style: ButtonGroupStyle = ButtonGroupStyle.Filled,
    horizontalArrangement: Arrangement.Horizontal = ButtonGroupDefaults.HorizontalArrangement,
    initialSelection: Int = 0,
    onSelectionChange: (index: Int) -> Unit
) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(initialSelection) }

    ButtonGroup(
        modifier = modifier,
        overflowIndicator = { },
        horizontalArrangement = horizontalArrangement
    ) {
        entries.forEachIndexed { index, entry ->
            val checked = index == selectedIndex

            when(style) {
                ButtonGroupStyle.Filled -> {
                    this.toggleableItem(
                        checked = checked,
                        label = entry.label,
                        onCheckedChange = {
                            selectedIndex = index
                            onSelectionChange(index)
                        },
                        icon = entry.icon,
                        weight = entry.weight,
                        enabled = entry.enabled
                    )
                }
                ButtonGroupStyle.Tonal -> {
                    this.customItem(
                        buttonGroupContent = {
                            TonalToggleButton(
                                modifier = if(!entry.weight.isNaN()) Modifier.weight(entry.weight) else Modifier,
                                checked = checked,
                                enabled = entry.enabled,
                                onCheckedChange ={
                                    selectedIndex = index
                                    onSelectionChange(index)
                                }
                            ) {
                                entry.icon?.let { it() }
                                if(entry.icon != null) Spacer(modifier = Modifier.width(8.dp))
                                Text(text = entry.label)
                            }
                        },
                        menuContent = { }
                    )
                }
            }
        }
    }
}