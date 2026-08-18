/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       XpCorrectionCard.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   18.08.26, 20:45
 */

package dev.bittim.valolink.feature.activity.ui.components.xp

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import dev.bittim.valolink.core.ui.Spacing

/**
 * Displays an XP correction value in a styled card.
 *
 * @param modifier The modifier applied to the card.
 * @param elevation The card's tonal elevation.
 * @param shape The card's shape.
 * @param xp The XP correction value to display.
 */
@Composable
fun XpCorrectionCard(
    modifier: Modifier = Modifier,
    elevation: Dp = Spacing.xxs,
    shape: Shape = MaterialTheme.shapes.medium,
    xp: Int?
) {
    Surface(
        modifier = modifier,
        shape = shape,
        tonalElevation = elevation
    ) {
        Column {
            Text(
                text = "XP Correction",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = xp.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
@Preview
fun XpCorrectionCardPreview() {
    MaterialTheme {
        Surface {
            XpCorrectionCard(
                xp = 1000
            )
        }
    }
}