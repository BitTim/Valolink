/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       ColorUtil.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.main.ui.util

import android.graphics.Color
import androidx.core.graphics.alpha


fun parseAndSaturateColor(
    colorString: String,
    saturation: Float,
): Int {
    val inColor = Color.parseColor(colorString)
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