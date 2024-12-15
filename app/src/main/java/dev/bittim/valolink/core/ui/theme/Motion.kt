/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       Motion.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

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