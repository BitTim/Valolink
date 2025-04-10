/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ColorUtil.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   11.04.25, 00:25
 */

package dev.bittim.valolink.main.ui.util

import android.graphics.Color
import androidx.core.graphics.alpha
import androidx.core.graphics.toColorInt


fun parseAndSaturateColor(
    colorString: String,
    saturation: Float,
): Int {
    val inColor = colorString.toColorInt()
    val hsv = FloatArray(3)
    Color.colorToHSV(
        inColor,
        hsv
    )
    hsv[1] *= saturation

    return Color.HSVToColor(
        inColor.alpha,
        hsv
    )
}
