package dev.bittim.valolink.core.ui.theme

import androidx.compose.animation.core.CubicBezierEasing

data object Motion {
    data object Duration {
        const val LONG_1 = 300
    }

    data object Easing {
        val motionEasingStandard = CubicBezierEasing(
            0.4f,
            0.0f,
            0.2f,
            1f
        )
    }
}