/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RankCluster.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   06.07.25, 02:52
 */

package dev.bittim.valolink.core.ui.components.rankCard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingFlat
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.components.ShaderGradientBackdrop
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.core.ui.util.annotations.ComponentPreviewAnnotations
import dev.bittim.valolink.core.ui.util.color.ShaderGradient
import kotlin.math.absoluteValue

@Composable
fun RankCluster(
    modifier: Modifier = Modifier,
    rankName: String,
    isUnranked: Boolean,
    rr: Int = 0,
    deltaRR: Int = 0,
    matchesPlayed: Int = 0,
    matchesNeeded: Int = 0
) {
    val animatedProgress: Float by animateFloatAsState(
        targetValue = if (!isUnranked) (rr / 100f) else (matchesPlayed.toFloat() / matchesNeeded.toFloat()),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "RR progress animation"
    )

    val trendPrefix = if (deltaRR < 0) "-" else if (deltaRR == 0) "±" else "+"
    val trendIcon =
        if (deltaRR < 0) {
            Icons.AutoMirrored.Default.TrendingDown
        } else if (deltaRR == 0) {
            Icons.AutoMirrored.Default.TrendingFlat
        } else {
            Icons.AutoMirrored.Default.TrendingUp
        }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
    ) {
        Text(text = rankName, style = MaterialTheme.typography.titleMedium, color = Color.White)

        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            trackColor = Color.Black.copy(alpha = 0.3f),
            progress = { animatedProgress }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = if (!isUnranked) {
                    "$rr " + UiText.StringResource(R.string.unit_rr).asString()
                } else {
                    "$matchesPlayed / $matchesNeeded " + UiText.StringResource(R.string.unit_match_plural)
                        .asString()
                },
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                if (!isUnranked) {
                    Text(
                        text = "$trendPrefix ${deltaRR.absoluteValue}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )

                    Icon(trendIcon, contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}

@ComponentPreviewAnnotations
@Composable
fun RankClusterPreview() {
    ValolinkTheme {
        ShaderGradientBackdrop(
            modifier = Modifier.height(68.dp),
            isDisabled = false,
            gradient = ShaderGradient(
                "4f514fff",
                "828282ff"
            )
        ) {
            Column {
                RankCluster(
                    modifier = Modifier.wrapContentSize(),
                    rankName = "Iron 2",
                    isUnranked = false,
                    rr = 64,
                    deltaRR = 0,
                )

                Spacer(modifier = Modifier.height(Spacing.l))


                RankCluster(
                    modifier = Modifier.wrapContentSize(),
                    rankName = "Unranked",
                    isUnranked = true,
                    matchesPlayed = 2,
                    matchesNeeded = 5,
                )
            }
        }
    }
}
