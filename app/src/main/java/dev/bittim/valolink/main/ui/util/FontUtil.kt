package dev.bittim.valolink.main.ui.util

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