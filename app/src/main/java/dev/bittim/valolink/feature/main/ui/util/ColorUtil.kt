package dev.bittim.valolink.feature.main.ui.util

import android.graphics.Color
import androidx.core.graphics.alpha


fun parseAndSaturateColor(colorString: String, saturation: Float): Int {
    val inColor = Color.parseColor(colorString)
    val hsv = FloatArray(3)
    Color.colorToHSV(inColor, hsv)
    hsv[1] *= saturation

    return Color.HSVToColor(inColor.alpha, hsv)
}