/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       FontUtil.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 17:30
 */

package dev.bittim.valolink.core.ui.util

import android.content.res.Configuration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

fun getScaledLineHeightFromFontStyle(
    density: Density,
    configuration: Configuration,
    textStyle: TextStyle,
): Dp {
    return with(density) {
        textStyle.lineHeight.toDp() * configuration.fontScale
    }
}
