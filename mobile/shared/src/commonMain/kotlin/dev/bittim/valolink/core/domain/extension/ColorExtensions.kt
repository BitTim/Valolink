/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ColorExtensions.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   01.08.26, 12:29
 */

package dev.bittim.valolink.core.domain.extension

import androidx.compose.ui.graphics.Color

sealed class ColorFormat(val length: Int) {
    data object RGB : ColorFormat(6)
    data object ARGB : ColorFormat(8)
    data object RGBA : ColorFormat(8)
}

@Suppress("MagicNumber")
fun Color.Companion.parseColor(value: String, format: ColorFormat): Color {
    val clean = value.removePrefix("#").uppercase()

    if (clean.length != format.length) {
        throw IllegalArgumentException("Invalid Hex color: $value")
    }

    val color = when (format) {
        ColorFormat.RGB -> {
            val r = clean.substring(0, 2).toInt(16)
            val g = clean.substring(2, 4).toInt(16)
            val b = clean.substring(4, 6).toInt(16)
            Color(red = r, green = g, blue = b, alpha = 255)
        }

        ColorFormat.ARGB, ColorFormat.RGBA -> {
            val c1 = clean.substring(0, 2).toInt(16)
            val c2 = clean.substring(2, 4).toInt(16)
            val c3 = clean.substring(4, 6).toInt(16)
            val c4 = clean.substring(6, 8).toInt(16)

            when (format) {
                ColorFormat.ARGB -> Color(alpha = c1, red = c2, green = c3, blue = c4)
                ColorFormat.RGBA -> Color(red = c1, green = c2, blue = c3, alpha = c4)
            }
        }
    }

    return color
}

