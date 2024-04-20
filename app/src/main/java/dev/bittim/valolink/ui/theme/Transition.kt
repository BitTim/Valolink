package dev.bittim.valolink.ui.theme

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut

data object Transition {
    val topLevelEnter = fadeIn(
        tween(
            durationMillis = Motion.Duration.LONG_1,
            easing = Motion.Easing.motionEasingStandard
        )
    )

    val topLevelExit = fadeOut(
        tween(
            durationMillis = Motion.Duration.LONG_1, easing = Motion.Easing.motionEasingStandard
        )
    )

    val forward = fadeIn(
        tween(
            durationMillis = Motion.Duration.LONG_1, easing = Motion.Easing.motionEasingStandard
        )
    )

    val backward = fadeOut(
        tween(
            durationMillis = Motion.Duration.LONG_1,
            easing = Motion.Easing.motionEasingStandard
        )
    )
}