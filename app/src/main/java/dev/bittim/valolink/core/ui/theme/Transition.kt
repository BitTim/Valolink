/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       Transition.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   18.12.24, 02:29
 */

package dev.bittim.valolink.core.ui.theme

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

enum class Transition(
    val enter: EnterTransition,
    val exit: ExitTransition,
    val popEnter: EnterTransition,
    val popExit: ExitTransition
) {
    TopLevel(
        enter = fadeIn(
            tween(
                durationMillis = Motion.Duration.MEDIUM_2,
                delayMillis = Motion.Duration.SHORT_2,
                easing = Motion.Easing.emphasizedDecelerate,
            )
        ),

        exit = fadeOut(
            tween(
                durationMillis = Motion.Duration.SHORT_2,
                easing = Motion.Easing.emphasizedAccelerate,
            )
        ),

        popEnter = fadeIn(
            tween(
                durationMillis = Motion.Duration.MEDIUM_2,
                delayMillis = Motion.Duration.SHORT_2,
                easing = Motion.Easing.emphasizedDecelerate
            )
        ),

        popExit = fadeOut(
            tween(
                durationMillis = Motion.Duration.SHORT_2,
                easing = Motion.Easing.emphasizedAccelerate,
            )
        )
    ),

    ForwardBackward(
        enter = fadeIn(
            tween(
                durationMillis = Motion.Duration.MEDIUM_2,
                delayMillis = Motion.Duration.SHORT_2,
                easing = Motion.Easing.emphasizedDecelerate,
            )
        ) + slideInHorizontally(
            animationSpec = tween(
                durationMillis = Motion.Duration.MEDIUM_2,
                delayMillis = Motion.Duration.SHORT_2,
                easing = Motion.Easing.emphasizedDecelerate,
            ),
            initialOffsetX = { it / 2 }
        ),

        exit = fadeOut(
            tween(
                durationMillis = Motion.Duration.SHORT_2,
                easing = Motion.Easing.emphasizedAccelerate,
            )
        ) + slideOutHorizontally(
            animationSpec = tween(
                durationMillis = Motion.Duration.SHORT_2,
                easing = Motion.Easing.emphasizedAccelerate,
            ),
            targetOffsetX = { -it / 2 }
        ),

        popEnter = fadeIn(
            tween(
                durationMillis = Motion.Duration.MEDIUM_2,
                delayMillis = Motion.Duration.SHORT_2,
                easing = Motion.Easing.emphasizedDecelerate,
            )
        ) + slideInHorizontally(
            animationSpec = tween(
                durationMillis = Motion.Duration.MEDIUM_2,
                delayMillis = Motion.Duration.SHORT_2,
                easing = Motion.Easing.emphasizedDecelerate,
            ),
            initialOffsetX = { -it / 2 }
        ),

        popExit = fadeOut(
            tween(
                durationMillis = Motion.Duration.SHORT_2,
                easing = Motion.Easing.emphasizedAccelerate,
            )
        ) + slideOutHorizontally(
            animationSpec = tween(
                durationMillis = Motion.Duration.SHORT_2,
                easing = Motion.Easing.emphasizedAccelerate,
            ),
            targetOffsetX = { it / 2 }
        )
    )
}