/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       IconCardLayout.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.06.26, 18:52
 */

package dev.bittim.valolink.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import dev.bittim.valolink.core.ui.Spacing

@Composable
fun IconCardLayout(
    modifier: Modifier = Modifier,
    elevation: Dp = Spacing.xxs,
    shape: Shape = MaterialTheme.shapes.medium,
    spacing: Dp = Spacing.s,
    icon: @Composable (modifier: Modifier) -> Unit,
    content: @Composable (modifier: Modifier) -> Unit
) {
    var iconSize by remember { mutableStateOf(0) }

    Surface(
        modifier = modifier,
        shape = shape,
        tonalElevation = elevation
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            icon(Modifier.size(with(LocalDensity.current) { iconSize.toDp() }))
            content(Modifier.onSizeChanged { iconSize = it.height })
        }
    }
}