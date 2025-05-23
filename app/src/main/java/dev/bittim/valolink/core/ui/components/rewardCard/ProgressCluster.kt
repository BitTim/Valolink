/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ProgressCluster.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   22.04.25, 03:44
 */

package dev.bittim.valolink.core.ui.components.rewardCard

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.getProgressPercent

@Composable
fun ProgressCluster(
    modifier: Modifier = Modifier,
    progress: Int,
    total: Int,
    unit: String,
    isMonochrome: Boolean,
    isCompact: Boolean,
) {
    val percentage = getProgressPercent(
        progress,
        total
    )

    val animatedProgress: Float by animateFloatAsState(
        targetValue = (percentage / 100f),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = ""
    )

    if (isCompact) {
        CircularProgressIndicator(
            modifier = modifier,
            color = if (isMonochrome) Color.White else MaterialTheme.colorScheme.primary,
            trackColor = if (isMonochrome) Color.Black.copy(alpha = 0.3f) else MaterialTheme.colorScheme.primaryContainer,
            progress = { animatedProgress }
        )
    } else {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(Spacing.s, Alignment.CenterVertically)
        ) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = if (isMonochrome) Color.White else MaterialTheme.colorScheme.primary,
                trackColor = if (isMonochrome) Color.Black.copy(alpha = 0.3f) else MaterialTheme.colorScheme.primaryContainer,
                progress = { animatedProgress }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f),
                    textAlign = TextAlign.Start,
                    text = "$progress / $total $unit",
                    color = if (isMonochrome) Color.White else MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelLarge
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    textAlign = TextAlign.End,
                    text = "$percentage %",
                    color = if (isMonochrome) Color.White else MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProgressClusterPreview() {
    ValolinkTheme {
        Surface {
            Box(
                modifier = Modifier
                    .width(412.dp)
                    .padding(16.dp)
            ) {
                ProgressCluster(
                    progress = 60,
                    total = 100,
                    unit = "XP",
                    isMonochrome = false,
                    isCompact = false
                )
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
fun MonochromeProgressClusterPreview() {
    ValolinkTheme {
        Surface {
            Box(
                modifier = Modifier
                    .width(412.dp)
                    .background(
                        Brush.horizontalGradient(listOf(Color.DarkGray, Color.Gray))
                    )
                    .padding(16.dp)
            ) {
                ProgressCluster(
                    progress = 3,
                    total = 10,
                    unit = "",
                    isMonochrome = true,
                    isCompact = false
                )
            }
        }
    }
}


@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CompactProgressClusterPreview() {
    ValolinkTheme {
        Surface {
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .padding(16.dp)
            ) {
                ProgressCluster(
                    modifier = Modifier.size(Spacing.xxl),
                    progress = 60,
                    total = 100,
                    unit = "XP",
                    isMonochrome = false,
                    isCompact = true
                )
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
fun CompactMonochromeProgressClusterPreview() {
    ValolinkTheme {
        Surface {
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .background(
                        Brush.horizontalGradient(listOf(Color.DarkGray, Color.Gray))
                    )
                    .padding(16.dp)
            ) {
                ProgressCluster(
                    modifier = Modifier.size(Spacing.xxl),
                    progress = 3,
                    total = 10,
                    unit = "",
                    isMonochrome = true,
                    isCompact = true
                )
            }
        }
    }
}
