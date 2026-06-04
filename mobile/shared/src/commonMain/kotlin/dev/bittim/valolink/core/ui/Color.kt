/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       Color.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   04.06.26, 11:05
 */

package dev.bittim.valolink.core.ui

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class ColorSchemeExtension(
    val win: Color = Color(0xFF34C759),
    val onWin: Color = Color.White,
    val loss: Color = Color(0xFFFF3B30),
    val onLoss: Color = Color.White
)

val LocalColorSchemeExtension = staticCompositionLocalOf { ColorSchemeExtension() }