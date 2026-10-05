/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ScoreChip.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   05.10.26, 03:05
 */

package dev.bittim.valolink.feature.activity.ui.components.match

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.core.domain.model.MatchOutcome
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.colors

object ScoreChipDefaults {
    val iconHeight: Dp = 16.dp
    val iconPadding: PaddingValues = PaddingValues(Spacing.xs)
    val textPadding: PaddingValues = PaddingValues(horizontal = Spacing.s, vertical = Spacing.xs)
    val cornerRadius: Dp = Spacing.m
    val connectedCornerRadius: Dp = Spacing.xs
    val connectedSpacing: Dp = Spacing.xxs
    val skeletonWidth: Dp = 48.dp

    val textStyle: TextStyle
        @Composable get() = MaterialTheme.typography.labelMedium

    val textHeight: Dp
        @Composable get() = with(LocalDensity.current) { textStyle.lineHeight.toDp() }

    val height: Dp
        @Composable get() = maxOf(
            iconHeight + iconPadding.calculateTopPadding() + iconPadding.calculateBottomPadding(),
            textHeight + textPadding.calculateTopPadding() + textPadding.calculateBottomPadding()
        )
}

@Composable
fun ScoreChip(
    modifier: Modifier = Modifier,
    state: ScoreChipState,
) {
    val colors = state.outcome.colors()
    val bgColor by animateColorAsState(colors.bg)
    val fgColor by animateColorAsState(colors.fg)

    val innerCornerRadius by animateDpAsState(
        targetValue = if(state.wasSurrender) ScoreChipDefaults.connectedCornerRadius else ScoreChipDefaults.cornerRadius
    )

    Row (
        modifier = modifier.height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ScoreChipDefaults.connectedSpacing)
    ) {
        AnimatedVisibility(
            visible = state.wasSurrender
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(
                        topStart = ScoreChipDefaults.cornerRadius,
                        topEnd = innerCornerRadius,
                        bottomStart = ScoreChipDefaults.cornerRadius,
                        bottomEnd = innerCornerRadius
                    ))
                    .background(color = bgColor)
            ) {
                Icon(
                    modifier = Modifier
                        .padding(ScoreChipDefaults.iconPadding)
                        .size(ScoreChipDefaults.iconHeight),
                    imageVector = Icons.Default.Flag,
                    tint = fgColor,
                    contentDescription = null
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxHeight()
                .clip(RoundedCornerShape(
                    topStart = innerCornerRadius,
                    topEnd = ScoreChipDefaults.cornerRadius,
                    bottomStart = innerCornerRadius,
                    bottomEnd = ScoreChipDefaults.cornerRadius
                ))
                .background(color = bgColor)
        ) {
            AnimatedContent(
                targetState = state.score ?: ""
            ) {
                Text(
                    modifier = Modifier
                        .padding(ScoreChipDefaults.textPadding)
                        .align(Alignment.Center),
                    text = it,
                    color = fgColor,
                    style = ScoreChipDefaults.textStyle,
                )
            }
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