/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MapCard.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   09.06.26, 01:57
 */

package dev.bittim.valolink.feature.activity.ui.components.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.bittim.valolink.core.ui.Spacing
import kotlin.uuid.Uuid

data object MapCard {
    val height = 128.dp
}

@Composable
fun MapCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    height: Dp = MapCard.height,
    elevation: Dp = Spacing.xxs,
    state: MapCardState
) {
    Surface(
        modifier = modifier.height(height),
        shape = shape,
        tonalElevation = elevation
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize()
                .blur(Spacing.xxs),
            model = state.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier.fillMaxSize()
                .background(Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.5f)
                    )
                ))
                .padding(Spacing.s),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = state.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            AnimatedVisibility(visible = state.coordinates != null) {
                Text(
                    text = state.coordinates ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
@Preview
fun MapCardPreview() {
    MaterialTheme {
        Surface {
            MapCard(
                modifier = Modifier.padding(Spacing.l),
                state = MapCardState(
                    uuid = Uuid.random(),
                    title = "Sample Map",
                    coordinates = "123.4567E, 89.1234S",
                    imageUrl = ""
                )
            )
        }
    }
}