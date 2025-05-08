/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ColorUtil.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   17.04.25, 14:53
 */

package dev.bittim.valolink.core.ui.util.color

import android.graphics.Color
import androidx.core.graphics.alpha
import androidx.core.graphics.toColorInt


fun convertRGBAtoARGB(colorString: String, leadingHash: Boolean = false): String {
    try {
        val argb = colorString.substring(6) + colorString.substring(0, 6)
        return if (leadingHash) "#$argb" else argb
    } catch (_: Exception) {
        return if (leadingHash) "#ffff00ff" else "ffff00ff"
    }
}

fun parseAndSaturateColor(
    argbString: String,
    saturation: Float,
): Int {
    val inColor = argbString.toColorInt()
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
