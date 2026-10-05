/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchCard.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   05.10.26, 03:05
 */

package dev.bittim.valolink.feature.activity.ui.components.match

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import dev.bittim.valolink.core.domain.extension.toLocalizedString
import dev.bittim.valolink.core.domain.model.MatchOutcome
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.IconCardLayout
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.match_card_xp_placeholder
import valolink.shared.generated.resources.unit_xp
import kotlin.time.Clock

object MatchCardDefaults {
    val contentPadding: PaddingValues = PaddingValues(Spacing.s)
    val titleSpacing: Dp = Spacing.xs
    val contentSpacing: Dp = Spacing.xxs

    val modeTextStyle: TextStyle
        @Composable get() = MaterialTheme.typography.titleMedium

    val detailTextStyle: TextStyle
        @Composable get() = MaterialTheme.typography.labelMedium

    val xpTextStyle: TextStyle
        @Composable get() = MaterialTheme.typography.labelLarge

    val modeTextHeight: Dp
        @Composable get() = with(LocalDensity.current) {
            modeTextStyle.lineHeight.toDp()
        }

    val detailTextHeight: Dp
        @Composable get() = with(LocalDensity.current) {
            detailTextStyle.lineHeight.toDp()
        }

    val xpTextHeight: Dp
        @Composable get() = with(LocalDensity.current) {
            xpTextStyle.lineHeight.toDp()
        }
}

@Composable
fun MatchCard(
    modifier: Modifier = Modifier,
    state: MatchCardState
) {
    IconCardLayout(
        modifier = modifier,
        icon = { iconModifier ->
            MatchIcon(
                modifier = iconModifier,
                state = state.iconState
            )
        },
        content = { contentModifier ->
            Box(
                modifier = contentModifier.fillMaxWidth()
                    .padding(MatchCardDefaults.contentPadding)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(MatchCardDefaults.titleSpacing)
                ) {
                    AnimatedContent(
                        targetState = state.modeName
                    ) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(MatchCardDefaults.contentSpacing)
                    ) {
                        Row {
                            AnimatedContent(
                                targetState = state.mapName
                            ) {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Text(
                                text = " • ",
                                style = MatchCardDefaults.detailTextStyle,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            AnimatedContent(
                                targetState = state.time
                            ) {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        AnimatedContent(
                            targetState = "${state.xp ?: stringResource(Res.string.match_card_xp_placeholder)} ${stringResource(Res.string.unit_xp)}"
                        ) {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    ScoreChip(
                        state = state.scoreChipState
                    )
                }
            }
        }
    )
}

@Composable
@Preview
fun MatchCardPreview() {
    MaterialTheme {
        Surface {
            val outcome = MatchOutcome.Win

            MatchCard(
                modifier = Modifier.fillMaxWidth()
                    .padding(Spacing.l),
                state = MatchCardState(
                    iconState = MatchIconState(
                        outcome = outcome,
                        mapImageUrl = null,
                        iconUrl = null,
                        rrChipState = RrChipState(
                            rr = 23,
                            rankChanged = true
                        )
                    ),
                    scoreChipState = ScoreChipState(
                        outcome = outcome,
                        wasSurrender = true,
                        score = "6 - 2"
                    ),
                    modeName = "Standard",
                    mapName = "Bind",
                    time = Clock.System.now().toLocalizedString(),
                    xp = 0,
                )
            )
        }
    }
}