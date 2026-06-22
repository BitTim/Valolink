/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       IntSignColors.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   04.06.26, 18:23
 */

package dev.bittim.valolink.core.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class IntSignStyle(
    val symbol: String,
    val bg: Color,
    val fg: Color
)

@Composable
fun Int.signStyle(): IntSignStyle = when {
    (this < 0) -> IntSignStyle(
        symbol = "-",
        bg = MaterialTheme.colorScheme.error,
        fg = MaterialTheme.colorScheme.onError
    )
    (this > 0) -> IntSignStyle(
        symbol = "+",
        bg = MaterialTheme.colorScheme.tertiary,
        fg = MaterialTheme.colorScheme.onTertiary
    )
    else -> IntSignStyle(
        symbol = "±",
        bg = MaterialTheme.colorScheme.background,
        fg = MaterialTheme.colorScheme.onBackground
    )
}