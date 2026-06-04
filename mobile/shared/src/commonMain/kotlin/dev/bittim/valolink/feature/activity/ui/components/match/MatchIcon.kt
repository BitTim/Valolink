/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchIcon.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   04.06.26, 12:17
 */

package dev.bittim.valolink.feature.activity.ui.components.match

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import dev.bittim.valolink.core.domain.model.MatchOutcome
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.colors
import kotlin.math.sqrt

@Composable
fun MatchIcon(
    modifier: Modifier = Modifier,
    state: MatchIconState
) {
    val colors = state.outcome.colors()
    val bgColor by animateColorAsState(colors.bg)

    Box(
        modifier = modifier.aspectRatio(1f, matchHeightConstraintsFirst = true)
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = state.mapImageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier.fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .drawBehind {
                    val radius = size.width * sqrt(2f)
                    val brush = Brush.radialGradient(
                        colors = listOf(
                            bgColor,
                            Color.Transparent
                        ),
                        center = Offset(size.width, 0f),
                        radius = radius
                    )
                    drawRect(brush)
                }
                .alpha(0.5f)
        )

        AsyncImage(
            modifier = Modifier.fillMaxSize()
                .padding(Spacing.m),
            model = state.modeIconUrl,
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
@Preview
fun MatchIconPreview() {
    MaterialTheme {
        Surface {
            MatchIcon(
                modifier = Modifier,
                state = MatchIconState(
                    outcome = MatchOutcome.Win,
                    mapImageUrl = null,
                    modeIconUrl = null
                )
            )
        }
    }
}