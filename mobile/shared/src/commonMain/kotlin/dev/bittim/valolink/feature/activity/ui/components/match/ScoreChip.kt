/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ScoreChip.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   04.06.26, 12:17
 */

package dev.bittim.valolink.feature.activity.ui.components.match

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.core.domain.model.MatchOutcome
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.colors

@Composable
fun ScoreChip(
    modifier: Modifier = Modifier,
    state: ScoreChipState,
) {
    val colors = state.outcome.colors()
    val bgColor by animateColorAsState(colors.bg)
    val fgColor by animateColorAsState(colors.fg)

    val innerCornerRadius by animateDpAsState(
        targetValue = if(state.wasSurrender) Spacing.xs else Spacing.m
    )

    Row (
        modifier = modifier.height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)
    ) {
        AnimatedVisibility(
            visible = state.wasSurrender
        ) {
            Box(
                modifier = Modifier.clip(
                    RoundedCornerShape(
                        topStart = Spacing.m,
                        topEnd = innerCornerRadius,
                        bottomStart = Spacing.m,
                        bottomEnd = innerCornerRadius
                    )
                )
                    .background(color = bgColor)
            ) {
                Icon(
                    modifier = Modifier
                        .padding(horizontal = Spacing.s, vertical = Spacing.xs)
                        .size(20.dp),
                    imageVector = Icons.Default.Flag,
                    tint = fgColor,
                    contentDescription = "Surrender"
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxHeight()
                .clip(RoundedCornerShape(
                    topStart = innerCornerRadius,
                    topEnd = Spacing.m,
                    bottomStart = innerCornerRadius,
                    bottomEnd = Spacing.m
                ))
                .background(color = bgColor)
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = Spacing.s, vertical = Spacing.xs)
                    .align(Alignment.Center),
                text = state.score,
                color = fgColor,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
@Preview
fun ScoreChipPreview() {
    MaterialTheme {
        Surface {
            ScoreChip(
                modifier = Modifier.padding(Spacing.l),
                state = ScoreChipState(
                    outcome = MatchOutcome.Win,
                    wasSurrender = true,
                    "14 - 14"
                )
            )
        }
    }
}