/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       PulseAnimationExtension.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.08.26, 18:59
 */

package dev.bittim.valolink.core.ui.extensions

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.Spacing

fun Modifier.pulseAnimation(durationMillis: Int = 1000): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "Loading pulse")
    val color by transition.animateColor(
        initialValue = MaterialTheme.colorScheme.surfaceContainerLow,
        targetValue = MaterialTheme.colorScheme.surfaceContainerHigh,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis),
            repeatMode = RepeatMode.Reverse,
        ), label = "Pulse animation"
    )

    drawBehind { drawRect(color = color) }
}

@Preview
@Composable
fun PreviewPulseAnimation() {
    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier
                    .padding(Spacing.l)
                    .aspectRatio(8f/1f)
                    .clip(MaterialTheme.shapes.medium)
                    .pulseAnimation()
            )
        }
    }
}