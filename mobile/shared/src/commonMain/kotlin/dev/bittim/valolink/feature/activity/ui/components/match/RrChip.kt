/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       RrChip.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   29.06.26, 02:09
 */

package dev.bittim.valolink.feature.activity.ui.components.match

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.signStyle
import kotlin.math.absoluteValue

@Composable
fun RrChip(
    modifier: Modifier = Modifier,
    state: RrChipState
) {
    val signStyle = state.rr.signStyle()
    val fgColor by animateColorAsState(signStyle.fg)
    val bgColor by animateColorAsState(signStyle.bg)
    val showIcon = remember(state.rankChanged, state.rr) { state.rankChanged && state.rr != 0 }

    val innerCornerRadius by animateDpAsState(
        targetValue = if(showIcon) Spacing.xs else Spacing.m
    )

    val startShape = RoundedCornerShape(
        topStart = Spacing.m,
        topEnd = innerCornerRadius,
        bottomStart = Spacing.m,
        bottomEnd = innerCornerRadius
    )

    val endShape = RoundedCornerShape(
        topStart = innerCornerRadius,
        topEnd = Spacing.m,
        bottomStart = innerCornerRadius,
        bottomEnd = Spacing.m
    )

    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)
    ) {
        AnimatedVisibility(showIcon) {
            Box(
                modifier = Modifier
                    .shadow(Spacing.xs, startShape)
                    .clip(startShape)
                    .background(color = bgColor)
            ) {
                Icon(
                    modifier = Modifier
                        .padding(Spacing.xs)
                        .size(16.dp),
                    imageVector = when {
                        (state.rr > 0) -> Icons.Outlined.KeyboardArrowUp
                        (state.rr < 0) -> Icons.Outlined.KeyboardArrowDown
                        else -> Icons.Outlined.QuestionMark
                    },
                    tint = fgColor,
                    contentDescription = null
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxHeight()
                .shadow(Spacing.xs, endShape)
                .clip(endShape)
                .background(color = bgColor)
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = Spacing.s, vertical = Spacing.xs)
                    .align(Alignment.Center),
                text = "${signStyle.symbol} ${state.rr.absoluteValue}",
                style = MaterialTheme.typography.labelMedium,
                color = fgColor
            )
        }
    }
}

@Composable
@Preview
fun RrChipPreview() {
    MaterialTheme {
        Surface {
            RrChip(
                modifier = Modifier,
                state = RrChipState(
                    rankChanged = true,
                    rr = 10
                )
            )
        }
    }
}