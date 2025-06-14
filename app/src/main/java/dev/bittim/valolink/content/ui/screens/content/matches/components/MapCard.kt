/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       MapCard.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.06.25, 02:07
 */package dev.bittim.valolink.content.ui.screens.content.matches.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.content.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.annotations.ComponentPreviewAnnotations
import dev.bittim.valolink.core.ui.util.extensions.modifier.pulseAnimation

data object MapCard {
    val height = 208.dp
    val width = 256.dp
}

data class MapCardData(
    val mapImage: String? = null,
    val mapName: String? = null,
    val mapCoordinates: String? = null,
)

@Composable
fun MapCard(
    modifier: Modifier = Modifier,
    data: MapCardData? = null,
) {
    Card(
        modifier = modifier
            .height(MapCard.height)
            .width(MapCard.width)
    ) {
        Crossfade(data) {
            if (it == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pulseAnimation()
                )
            } else {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (it.mapImage != null) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxSize(),
                            model = it.mapImage,
                            contentDescription = it.mapName,
                            contentScale = ContentScale.FillWidth,
                            alignment = Alignment.Center,
                            placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_map_image)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.5f)
                                    )
                                )
                            )
                            .padding(Spacing.l),
                        verticalArrangement = Arrangement.Bottom,
                    ) {
                        Text(
                            text = it.mapName ?: "",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White
                        )

                        Text(
                            text = it.mapCoordinates ?: "",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@ComponentPreviewAnnotations
@Composable
fun MapCardPreview() {
    ValolinkTheme {
        Surface {
            Column {
                MapCard(
                    modifier = Modifier.padding(16.dp),
                    data = MapCardData(
                        "",
                        "Haven",
                        "27°28'A'N,89°38'WZ'E"
                    )
                )

                MapCard(
                    modifier = Modifier.padding(16.dp),
                    data = null
                )
            }
        }
    }
}