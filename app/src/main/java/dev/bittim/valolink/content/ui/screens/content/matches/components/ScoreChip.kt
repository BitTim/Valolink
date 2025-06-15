/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ScoreChip.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   15.06.25, 16:00
 */

package dev.bittim.valolink.content.ui.screens.content.matches.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.R
import dev.bittim.valolink.core.domain.model.ScoreResult
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.core.ui.util.annotations.ComponentPreviewAnnotations

@Composable
fun ScoreChip(
    score: Int,
    enemyScore: Int?,
    surrender: Boolean,
    scoreResult: ScoreResult,
    compact: Boolean,
) {
    val containerColor = when (scoreResult) {
        ScoreResult.Win -> MaterialTheme.colorScheme.tertiary
        ScoreResult.Loss -> MaterialTheme.colorScheme.error
        ScoreResult.Draw -> MaterialTheme.colorScheme.surfaceContainer
    }
    val animatedContainerColor = animateColorAsState(containerColor)

    val objectColor = when (scoreResult) {
        ScoreResult.Win -> MaterialTheme.colorScheme.onTertiary
        ScoreResult.Loss -> MaterialTheme.colorScheme.onError
        ScoreResult.Draw -> MaterialTheme.colorScheme.onSurface
    }
    val animatedObjectColor = animateColorAsState(objectColor)

    val iconSize = if (compact) 20.dp else Spacing.xl
    val verticalPadding = if (compact) Spacing.xs else Spacing.s
    val horizontalPadding = if (compact) Spacing.s else Spacing.l
    val textStyle =
        if (compact) MaterialTheme.typography.labelLarge else MaterialTheme.typography.titleMedium

    val isPlacement by remember(enemyScore) {
        derivedStateOf { enemyScore == null }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
    ) {
        val cornerRadius = if (surrender) Spacing.xs else Spacing.m
        val animatedCornerRadius = animateDpAsState(cornerRadius)

        AnimatedContent(surrender) {
            if (it) {
                Surface(
                    shape = RoundedCornerShape(
                        topStart = Spacing.m,
                        topEnd = animatedCornerRadius.value,
                        bottomStart = Spacing.m,
                        bottomEnd = animatedCornerRadius.value
                    ),
                    color = animatedContainerColor.value,
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(verticalPadding)
                            .size(iconSize),
                        imageVector = Icons.Default.Flag,
                        contentDescription = null,
                        tint = animatedObjectColor.value
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .padding(verticalPadding)
                        .size(iconSize)
                ) { }
            }
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = animatedCornerRadius.value,
                topEnd = Spacing.m,
                bottomStart = animatedCornerRadius.value,
                bottomEnd = Spacing.m
            ),
            color = animatedContainerColor.value,
        ) {
            AnimatedContent(isPlacement) { isPlacement ->
                Row(
                    modifier = Modifier
                        .padding(vertical = verticalPadding, horizontal = horizontalPadding)
                        .height(iconSize),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)
                ) {
                    AnimatedContent(score) { score ->
                        Text(
                            text = if (isPlacement) {
                                "$score. ${UiText.StringResource(R.string.unit_place).asString()}"
                            } else "$score",
                            style = textStyle,
                            color = animatedObjectColor.value
                        )
                    }

                    if (!isPlacement) {
                        Text(
                            text = "-",
                            style = textStyle,
                            color = animatedObjectColor.value
                        )

                        AnimatedContent(enemyScore) { enemyScore ->
                            Text(
                                text = "$enemyScore",
                                style = textStyle,
                                color = animatedObjectColor.value
                            )
                        }
                    }
                }
            }
        }
    }
}

@ComponentPreviewAnnotations
@Composable
fun ScoreChipPreview() {
    ValolinkTheme {
        Surface {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.s)
            ) {
                Column(
                    modifier = Modifier.padding(Spacing.l),
                    verticalArrangement = Arrangement.spacedBy(Spacing.s)
                ) {
                    ScoreChip(
                        score = 13,
                        enemyScore = 4,
                        surrender = false,
                        scoreResult = ScoreResult.Win,
                        compact = false
                    )

                    ScoreChip(
                        score = 4,
                        enemyScore = 13,
                        surrender = false,
                        scoreResult = ScoreResult.Loss,
                        compact = false
                    )

                    ScoreChip(
                        score = 14,
                        enemyScore = 14,
                        surrender = false,
                        scoreResult = ScoreResult.Draw,
                        compact = false
                    )

                    ScoreChip(
                        score = 2,
                        enemyScore = 8,
                        surrender = true,
                        scoreResult = ScoreResult.Win,
                        compact = false
                    )

                    ScoreChip(
                        score = 9,
                        enemyScore = 4,
                        surrender = true,
                        scoreResult = ScoreResult.Loss,
                        compact = false
                    )

                    ScoreChip(
                        score = 1,
                        enemyScore = null,
                        surrender = false,
                        scoreResult = ScoreResult.Win,
                        compact = false
                    )

                    ScoreChip(
                        score = 3,
                        enemyScore = null,
                        surrender = false,
                        scoreResult = ScoreResult.Draw,
                        compact = false
                    )
                }

                Column(
                    modifier = Modifier.padding(Spacing.l),
                    verticalArrangement = Arrangement.spacedBy(Spacing.s)
                ) {
                    ScoreChip(
                        score = 13,
                        enemyScore = 4,
                        surrender = false,
                        scoreResult = ScoreResult.Win,
                        compact = true
                    )

                    ScoreChip(
                        score = 4,
                        enemyScore = 13,
                        surrender = false,
                        scoreResult = ScoreResult.Loss,
                        compact = true
                    )

                    ScoreChip(
                        score = 14,
                        enemyScore = 14,
                        surrender = false,
                        scoreResult = ScoreResult.Draw,
                        compact = true
                    )

                    ScoreChip(
                        score = 2,
                        enemyScore = 8,
                        surrender = true,
                        scoreResult = ScoreResult.Win,
                        compact = true
                    )

                    ScoreChip(
                        score = 9,
                        enemyScore = 4,
                        surrender = true,
                        scoreResult = ScoreResult.Loss,
                        compact = true
                    )

                    ScoreChip(
                        score = 1,
                        enemyScore = null,
                        surrender = false,
                        scoreResult = ScoreResult.Win,
                        compact = true
                    )

                    ScoreChip(
                        score = 3,
                        enemyScore = null,
                        surrender = false,
                        scoreResult = ScoreResult.Draw,
                        compact = true
                    )
                }
            }
        }
    }
}