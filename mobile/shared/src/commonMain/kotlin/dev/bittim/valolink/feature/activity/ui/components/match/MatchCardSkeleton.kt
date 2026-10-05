/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchCardSkeleton.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   05.10.26, 03:05
 */

package dev.bittim.valolink.feature.activity.ui.components.match

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.components.IconCardLayout
import dev.bittim.valolink.core.ui.extensions.pulseAnimation

@Composable
fun MatchCardSkeleton(
    modifier: Modifier = Modifier
) {
    IconCardLayout(
        modifier = modifier,
        icon = { iconModifier ->
            Box(
                modifier = iconModifier.pulseAnimation()
            )
        },
        content = { contentModifier ->
            Box(
                modifier = contentModifier.fillMaxWidth()
                    .padding(MatchCardDefaults.contentPadding),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(MatchCardDefaults.titleSpacing)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(0.7f)
                            .height(MatchCardDefaults.modeTextHeight)
                            .clip(MaterialTheme.shapes.medium)
                            .pulseAnimation()
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(MatchCardDefaults.contentSpacing)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(0.75f)
                                .height(MatchCardDefaults.detailTextHeight)
                                .clip(MaterialTheme.shapes.medium)
                                .pulseAnimation()
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth(0.4f)
                                .height(MatchCardDefaults.xpTextHeight)
                                .clip(MaterialTheme.shapes.medium)
                                .pulseAnimation()
                        )
                    }
                }

                Column(
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    ScoreChipSkeleton()
                }
            }
        }
    )
}

@Composable
@Preview
fun MatchCardSkeletonPreview() {
    MaterialTheme {
        Surface {
            MatchCardSkeleton(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}