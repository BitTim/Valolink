/*
 Copyright (c) 2025 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       RankIconCard.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   06.07.25, 02:52
 */

package dev.bittim.valolink.core.ui.components.rankCard

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.content.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.core.ui.components.ShaderGradientBackdrop
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.annotations.ComponentPreviewAnnotations
import dev.bittim.valolink.core.ui.util.color.ShaderGradient
import dev.bittim.valolink.core.ui.util.extensions.modifier.pulseAnimation

data object RankIconCard {
    val size = 116.dp
}

@Composable
fun RankIconCard(
    modifier: Modifier = Modifier,
    data: RankCardData? = null,
) {
    Card(
        modifier = modifier.size(RankIconCard.size),
    ) {
        Crossfade(targetState = data, label = "Data loading") { checkedData ->
            if (checkedData == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pulseAnimation()
                )
            } else {
                ShaderGradientBackdrop(
                    modifier = Modifier
                        .fillMaxSize(),
                    isDisabled = false,
                    gradient = ShaderGradient.fromRGBAList(checkedData.gradient),
                    backgroundImage = null
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Spacing.l),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .width(64.dp)
                                .aspectRatio(1f),
                            model = checkedData.rankIcon,
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            alignment = Alignment.Center,
                            placeholder = coilDebugPlaceholder(R.drawable.debug_rank_gold2)
                        )

                        Text(
                            text = checkedData.rankName,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }
}

@ComponentPreviewAnnotations
@Composable
fun RankIconCardPreview() {
    ValolinkTheme {
        Surface {
            RankIconCard(
                data = RankCardData(
                    rankName = "Gold 2",
                    rankIcon = "https://media.valorant-api.com/competitivetiers/564d8e28-c226-3180-6285-e48a390db8b1/4/largeicon.png",
                    gradient = listOf(
                        "eccf56ff",
                        "eec56aff",
                    ),
                ),
            )
        }
    }
}