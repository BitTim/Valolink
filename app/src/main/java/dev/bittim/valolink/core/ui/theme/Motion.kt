/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       Motion.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   18.12.24, 02:29
 */

package dev.bittim.valolink.core.ui.theme

import android.graphics.Path
import android.view.animation.PathInterpolator
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing

data object Motion {
    data object Duration {
        const val SHORT_1 = 50
        const val SHORT_2 = 100
        const val SHORT_3 = 150
        const val SHORT_4 = 200

        const val MEDIUM_1 = 250
        const val MEDIUM_2 = 300
        const val MEDIUM_3 = 350
        const val MEDIUM_4 = 400

        const val LONG_1 = 450
        const val LONG_2 = 500
        const val LONG_3 = 550
        const val LONG_4 = 600

        const val EXTRA_LONG_1 = 700
        const val EXTRA_LONG_2 = 800
        const val EXTRA_LONG_3 = 900
        const val EXTRA_LONG_4 = 1000
    }

    data object Easing {
        val motionEasingStandard = CubicBezierEasing(
            0.4f,
            0.0f,
            0.2f,
            1f
        )

        val emphasized = Easing {
            PathInterpolator(
                Path().also {
                    it.cubicTo(0.05f, 0f, 0.133333f, 0.06f, 0.166666f, 0.4f)
                    it.cubicTo(0.208333f, 0.82f, 0.25f, 1f, 1f, 1f)
                }
            ).getInterpolation(it)
        }

        val emphasizedDecelerate =
            Easing { PathInterpolator(0.05f, 0.7f, 0.1f, 1f).getInterpolation(it) }
        val emphasizedAccelerate =
            Easing { PathInterpolator(0.3f, 0f, 0.8f, 0.15f).getInterpolation(it) }

        val standard = Easing { PathInterpolator(0.2f, 0f, 0f, 1f).getInterpolation(it) }
        val standardDecelerate = Easing { PathInterpolator(0f, 0f, 0f, 1f).getInterpolation(it) }
        val standardAccelerate = Easing { PathInterpolator(0.3f, 0f, 1f, 1f).getInterpolation(it) }
    }
}