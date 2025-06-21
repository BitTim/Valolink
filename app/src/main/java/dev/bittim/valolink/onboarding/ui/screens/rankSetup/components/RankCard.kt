/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RankCard.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   21.06.25, 02:20
 */

package dev.bittim.valolink.onboarding.ui.screens.rankSetup.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

data object RankCard {
    val height = 102.dp
}

data class RankCardData(
    val rankName: String,
    val rankIcon: String?,
    val gradient: List<String>,
)

@Composable
fun RankCard(
    modifier: Modifier = Modifier,
    data: RankCardData?,
    isUnranked: Boolean,
    rr: Int = 0,
    deltaRR: Int = 0,
    matchesPlayed: Int = 0,
    matchesNeeded: Int = 0
) {
    Card(
        modifier = modifier.height(RankCard.height),
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
                    modifier = Modifier.fillMaxHeight(),
                    isDisabled = false,
                    gradient = ShaderGradient.fromRGBAList(checkedData.gradient),
                    backgroundImage = null
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(Spacing.l),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.l)
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

                        RankCluster(
                            modifier = Modifier
                                .fillMaxWidth(),
                            rankName = checkedData.rankName,
                            isUnranked = isUnranked,
                            rr = rr,
                            deltaRR = deltaRR,
                            matchesPlayed = matchesPlayed,
                            matchesNeeded = matchesNeeded
                        )
                    }
                }
            }
        }
    }
}

@ComponentPreviewAnnotations
@Composable
fun LargeRankCardPreview() {
    ValolinkTheme {
        Surface {
            Column {
                RankCard(
                    modifier = Modifier.width(300.dp),
                    data = RankCardData(
                        rankName = "Gold 2",
                        rankIcon = "https://media.valorant-api.com/competitivetiers/564d8e28-c226-3180-6285-e48a390db8b1/4/largeicon.png",
                        gradient = listOf(
                            "eccf56ff",
                            "eec56aff",
                        ),
                    ),
                    isUnranked = false,
                    rr = 24,
                    deltaRR = 0,
                )

                Spacer(modifier = Modifier.height(Spacing.l))


                RankCard(
                    modifier = Modifier.width(300.dp),
                    data = RankCardData(
                        rankName = "Unranked",
                        rankIcon = "https://media.valorant-api.com/competitivetiers/564d8e28-c226-3180-6285-e48a390db8b1/4/largeicon.png",
                        gradient = listOf(
                            "ffffffff",
                            "00000000",
                        ),
                    ),
                    isUnranked = true,
                    matchesPlayed = 2,
                    matchesNeeded = 5,
                )
            }
        }
    }
}
