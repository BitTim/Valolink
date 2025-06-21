/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RankChip.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   21.06.25, 03:13
 */

package dev.bittim.valolink.content.ui.screens.content.matches.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material3.Icon
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
import dev.bittim.valolink.content.domain.model.rank.Rank
import dev.bittim.valolink.content.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.core.ui.components.ShaderGradientBackdrop
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.core.ui.util.annotations.ComponentPreviewAnnotations
import dev.bittim.valolink.core.ui.util.color.ShaderGradient
import dev.bittim.valolink.core.ui.util.color.convertRGBAtoARGB
import dev.bittim.valolink.core.ui.util.color.parseARGBToColor

@Composable
fun RankChip(
    rank: Rank,
    deltaRR: Int,
    isRankChanged: Boolean,
    showRankName: Boolean,
    compact: Boolean,
) {
    val animatedRankColors = listOf(
        animateColorAsState(Color(parseARGBToColor(convertRGBAtoARGB(rank.color, true)))).value,
        animateColorAsState(
            Color(
                parseARGBToColor(
                    convertRGBAtoARGB(
                        rank.backgroundColor,
                        true
                    )
                )
            )
        ).value
    )

    val containerColor = when {
        deltaRR > 0 -> MaterialTheme.colorScheme.tertiary
        deltaRR < 0 -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.surfaceContainer
    }
    val animatedContainerColor = animateColorAsState(containerColor)

    val objectColor = when {
        deltaRR > 0 -> MaterialTheme.colorScheme.onTertiary
        deltaRR < 0 -> MaterialTheme.colorScheme.onError
        else -> MaterialTheme.colorScheme.onSurface
    }
    val animatedObjectColor = animateColorAsState(objectColor)

    val rankChangeIcon = when {
        deltaRR > 0 && isRankChanged -> Icons.Default.KeyboardDoubleArrowUp
        deltaRR < 0 && isRankChanged -> Icons.Default.KeyboardDoubleArrowDown
        else -> null
    }

    val iconSize = if (compact) 20.dp else Spacing.xl
    val verticalPadding = if (compact) Spacing.xs else Spacing.s
    val horizontalPadding = if (compact) Spacing.s else Spacing.l
    val textStyle =
        if (compact) MaterialTheme.typography.labelLarge else MaterialTheme.typography.titleMedium
    val smallTextStyle =
        if (compact) MaterialTheme.typography.labelSmall else MaterialTheme.typography.labelMedium

    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = Spacing.m,
                topEnd = Spacing.xs,
                bottomStart = Spacing.m,
                bottomEnd = Spacing.xs
            ),
        ) {
            ShaderGradientBackdrop(
                isDisabled = false,
                gradient = ShaderGradient.fromColorList(animatedRankColors),
            ) {
                Row(
                    modifier = Modifier.padding(verticalPadding),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        modifier = Modifier.size(iconSize),
                        model = rank.icon,
                        contentScale = ContentScale.Fit,
                        contentDescription = rank.name,
                        placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_rank_gold2)
                    )

                    AnimatedVisibility(showRankName) {
                        Text(
                            text = rank.name,
                            style = smallTextStyle,
                            color = Color.White,
                        )
                    }
                }
            }
        }

        AnimatedVisibility(rankChangeIcon != null) {
            Surface(
                shape = RoundedCornerShape(Spacing.xs),
                color = animatedContainerColor.value,
            ) {
                Icon(
                    modifier = Modifier
                        .padding(verticalPadding)
                        .size(iconSize),
                    imageVector = rankChangeIcon!!,
                    contentDescription = null,
                    tint = animatedObjectColor.value
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = Spacing.xs,
                topEnd = Spacing.m,
                bottomStart = Spacing.xs,
                bottomEnd = Spacing.m
            ),
            color = animatedContainerColor.value,
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = verticalPadding, horizontal = horizontalPadding)
                    .height(iconSize),
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                Text(
                    modifier = Modifier.animateContentSize(),
                    text = deltaRR.toString(),
                    style = textStyle,
                    color = animatedObjectColor.value
                )

                Text(
                    text = UiText.StringResource(R.string.unit_rr).asString(),
                    style = textStyle,
                    color = animatedObjectColor.value
                )
            }
        }
    }
}

@ComponentPreviewAnnotations
@Composable
fun RankChipPreview() {
    val gold2 = Rank(
        tier = 13,
        name = "GOLD 2",
        divisionName = "GOLD",
        color = "eccf56ff",
        backgroundColor = "eec56aff",
        icon = "https://media.valorant-api.com/competitivetiers/03621f52-342b-cf4e-4f86-9350a49c6d04/13/largeicon.png",
        triangleDownIcon = "https://media.valorant-api.com/competitivetiers/03621f52-342b-cf4e-4f86-9350a49c6d04/13/ranktriangledownicon.png",
        triangleUpIcon = "https://media.valorant-api.com/competitivetiers/03621f52-342b-cf4e-4f86-9350a49c6d04/13/ranktriangleupicon.png"
    )

    ValolinkTheme {
        Surface {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(Spacing.s)
            ) {
                Column(
                    modifier = Modifier.padding(Spacing.l),
                    verticalArrangement = Arrangement.spacedBy(Spacing.s)
                ) {
                    RankChip(
                        rank = gold2,
                        deltaRR = 24,
                        isRankChanged = false,
                        showRankName = false,
                        compact = false
                    )

                    RankChip(
                        rank = gold2,
                        deltaRR = -18,
                        isRankChanged = false,
                        showRankName = false,
                        compact = false
                    )

                    RankChip(
                        rank = gold2,
                        deltaRR = 0,
                        isRankChanged = false,
                        showRankName = false,
                        compact = false
                    )

                    RankChip(
                        rank = gold2,
                        deltaRR = 32,
                        isRankChanged = true,
                        showRankName = false,
                        compact = false
                    )

                    RankChip(
                        rank = gold2,
                        deltaRR = -27,
                        isRankChanged = true,
                        showRankName = false,
                        compact = false
                    )
                }

                Column(
                    modifier = Modifier.padding(Spacing.l),
                    verticalArrangement = Arrangement.spacedBy(Spacing.s)
                ) {
                    RankChip(
                        rank = gold2,
                        deltaRR = 24,
                        isRankChanged = false,
                        showRankName = true,
                        compact = false
                    )

                    RankChip(
                        rank = gold2,
                        deltaRR = -18,
                        isRankChanged = false,
                        showRankName = true,
                        compact = false
                    )

                    RankChip(
                        rank = gold2,
                        deltaRR = 0,
                        isRankChanged = false,
                        showRankName = true,
                        compact = false
                    )

                    RankChip(
                        rank = gold2,
                        deltaRR = 32,
                        isRankChanged = true,
                        showRankName = true,
                        compact = false
                    )

                    RankChip(
                        rank = gold2,
                        deltaRR = -27,
                        isRankChanged = true,
                        showRankName = true,
                        compact = false
                    )
                }

                Column(
                    modifier = Modifier.padding(Spacing.l),
                    verticalArrangement = Arrangement.spacedBy(Spacing.s)
                ) {
                    RankChip(
                        rank = gold2,
                        deltaRR = 24,
                        isRankChanged = false,
                        showRankName = false,
                        compact = true
                    )

                    RankChip(
                        rank = gold2,
                        deltaRR = -18,
                        isRankChanged = false,
                        showRankName = false,
                        compact = true
                    )

                    RankChip(
                        rank = gold2,
                        deltaRR = 0,
                        isRankChanged = false,
                        showRankName = false,
                        compact = true
                    )

                    RankChip(
                        rank = gold2,
                        deltaRR = 32,
                        isRankChanged = true,
                        showRankName = false,
                        compact = true
                    )

                    RankChip(
                        rank = gold2,
                        deltaRR = -27,
                        isRankChanged = true,
                        showRankName = false,
                        compact = true
                    )
                }

                Column(
                    modifier = Modifier.padding(Spacing.l),
                    verticalArrangement = Arrangement.spacedBy(Spacing.s)
                ) {
                    RankChip(
                        rank = gold2,
                        deltaRR = 24,
                        isRankChanged = false,
                        showRankName = true,
                        compact = true
                    )

                    RankChip(
                        rank = gold2,
                        deltaRR = -18,
                        isRankChanged = false,
                        showRankName = true,
                        compact = true
                    )

                    RankChip(
                        rank = gold2,
                        deltaRR = 0,
                        isRankChanged = false,
                        showRankName = true,
                        compact = true
                    )

                    RankChip(
                        rank = gold2,
                        deltaRR = 32,
                        isRankChanged = true,
                        showRankName = true,
                        compact = true
                    )

                    RankChip(
                        rank = gold2,
                        deltaRR = -27,
                        isRankChanged = true,
                        showRankName = true,
                        compact = true
                    )
                }
            }
        }
    }
}