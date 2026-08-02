/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       RankCard.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   01.08.26, 12:23
 */

package dev.bittim.valolink.feature.activity.ui.components.rank

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.IconCardLayout

/**
 * Displays a rank card with its image, name, division, and themed background.
 *
 * @param state The rank data and colors displayed by the card.
 */
@Composable
fun RankCard(
    modifier: Modifier = Modifier,
    state: RankCardState
) {
    IconCardLayout(
        modifier = modifier,
        icon = { iconModifier ->
            Box(
                modifier = iconModifier.aspectRatio(1f, matchHeightConstraintsFirst = true)
                    .clip(MaterialTheme.shapes.medium)
                    .background(brush = Brush.linearGradient(
                        colors = listOf(state.backgroundColor, state.color)
                    ))
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize()
                        .padding(Spacing.m),
                    model = state.imageUrl,
                    contentDescription = state.name,
                    contentScale = ContentScale.Fit
                )
            }
        },
        content = { contentModifier ->
            Column(
                modifier = contentModifier.fillMaxWidth()
                    .padding(Spacing.s),
                verticalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                Text(
                    text = state.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = state.division,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}

@Composable
@Preview
fun RankCardPreview() {
    MaterialTheme {
        Surface {
            RankCard(
                modifier = Modifier.fillMaxWidth()
                    .padding(Spacing.l),
                state = RankCardState(
                    tier = 0,
                    name = "Rank name",
                    division = "Rank division",
                    imageUrl = null,
                    color = MaterialTheme.colorScheme.primary,
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                )
            )
        }
    }
}