package dev.bittim.valolink.core.ui.util.extensions

import androidx.compose.ui.geometry.Rect
import kotlin.math.roundToInt

fun Rect.toAndroidRect(): android.graphics.Rect {
    return android.graphics.Rect(
        left.roundToInt(),
        top.roundToInt(),
        right.roundToInt(),
        bottom.roundToInt()
    )
}