/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       Transition.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.core.ui.theme

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
            durationMillis = Motion.Duration.LONG_1,
            easing = Motion.Easing.motionEasingStandard
        )
    )

    val forward = fadeIn(
        tween(
            durationMillis = Motion.Duration.LONG_1,
            easing = Motion.Easing.motionEasingStandard
        )
    )

    val backward = fadeOut(
        tween(
            durationMillis = Motion.Duration.LONG_1,
            easing = Motion.Easing.motionEasingStandard
        )
    )
}