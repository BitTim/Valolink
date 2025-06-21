/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ColorUtil.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   21.06.25, 02:20
 */

package dev.bittim.valolink.core.ui.util.color

import android.graphics.Color
import androidx.core.graphics.alpha
import androidx.core.graphics.toColorInt


fun convertRGBAtoARGB(colorString: String, leadingHash: Boolean = false): String {
    try {
        val strippedString = colorString.removePrefix("#")
        val argb = strippedString.substring(6) + strippedString.substring(0, 6)
        return if (leadingHash) "#$argb" else argb
    } catch (_: Exception) {
        return if (leadingHash) "#ffff00ff" else "ffff00ff"
    }
}

fun convertARGBtoRGBA(colorString: String, leadingHash: Boolean = false): String {
    try {
        val strippedString = colorString.removePrefix("#")
        val rgba = strippedString.substring(0, 6) + strippedString.substring(6)
        return if (leadingHash) "#$rgba" else rgba
    } catch (_: Exception) {
        return if (leadingHash) "#ff00ffff" else "ff00ffff"
    }
}

fun parseARGBToColor(argbString: String): Int {
    return parseAndSaturateARGBToColor(argbString, 1f)
}

fun parseAndSaturateARGBToColor(
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
