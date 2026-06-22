/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       NavTransitions.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   04.06.26, 12:28
 */

package dev.bittim.valolink.core.nav

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.navigation3.ui.NavDisplay

val fadeThrough = NavDisplay.transitionSpec {
    fadeIn(
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        ),
        initialAlpha = 0f
    ) togetherWith fadeOut(
        animationSpec = tween(
            durationMillis = 150,
            easing = FastOutLinearInEasing
        )
    )
} + NavDisplay.popTransitionSpec {
    fadeIn(
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        )
    ) togetherWith fadeOut(
        animationSpec = tween(
            durationMillis = 150,
            easing = FastOutLinearInEasing
        )
    )
}