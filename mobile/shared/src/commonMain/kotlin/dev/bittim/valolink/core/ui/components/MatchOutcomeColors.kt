/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchOutcomeColors.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   04.06.26, 11:52
 */

package dev.bittim.valolink.core.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.bittim.valolink.core.domain.model.MatchOutcome

data class MatchOutcomeColors(val bg: Color, val fg: Color)

@Composable
fun MatchOutcome.colors(): MatchOutcomeColors = when (this) {
    MatchOutcome.Draw -> MatchOutcomeColors(
        bg = MaterialTheme.colorScheme.background,
        fg = MaterialTheme.colorScheme.onBackground
    )
    MatchOutcome.Win -> MatchOutcomeColors(
        bg = MaterialTheme.colorScheme.tertiary,
        fg = MaterialTheme.colorScheme.onTertiary
    )
    MatchOutcome.Loss -> MatchOutcomeColors(
        bg = MaterialTheme.colorScheme.error,
        fg = MaterialTheme.colorScheme.onError
    )
}