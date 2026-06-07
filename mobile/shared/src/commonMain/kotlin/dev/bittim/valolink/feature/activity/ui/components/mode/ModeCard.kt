/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ModeCard.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.06.26, 20:08
 */

package dev.bittim.valolink.feature.activity.ui.components.mode

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.IconCardLayout
import kotlin.uuid.Uuid

@Composable
fun ModeCard(
    modifier: Modifier = Modifier,
    state: ModeCardState
) {
    IconCardLayout(
        modifier = modifier,
        icon = { iconModifier ->
            Box(
                modifier = iconModifier.aspectRatio(1f, matchHeightConstraintsFirst = true)
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize()
                        .padding(Spacing.m),
                    model = state.iconUrl,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
            }
        },
        content = { contentModifier ->
            Row(
                modifier = contentModifier.fillMaxWidth()
                    .padding(Spacing.s),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    Text(
                        text = state.title,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = state.duration ?: "",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                AnimatedVisibility(state.canBeRanked) {
                    Icon(
                        modifier = Modifier.padding(Spacing.s),
                        imageVector = Icons.Default.MilitaryTech,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        contentDescription = "Mode Icon"
                    )
                }
            }
        }
    )
}

@Composable
@Preview
fun ModeCardPreview() {
    MaterialTheme {
        Surface {
            ModeCard(
                modifier = Modifier.fillMaxWidth()
                    .padding(Spacing.l),
                state = ModeCardState(
                    uuid = Uuid.random(),
                    title = "Skirmish",
                    iconUrl = "",
                    duration = "5-10 MINS",
                    canBeRanked = true
                )
            )
        }
    }
}