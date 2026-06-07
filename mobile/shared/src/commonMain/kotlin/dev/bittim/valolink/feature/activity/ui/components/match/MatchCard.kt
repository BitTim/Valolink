/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchCard.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.06.26, 20:22
 */

package dev.bittim.valolink.feature.activity.ui.components.match

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.domain.extension.toLocalizedString
import dev.bittim.valolink.core.domain.model.MatchOutcome
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.IconCardLayout
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.unit_xp
import kotlin.time.Clock

@Composable
fun MatchCard(
    modifier: Modifier = Modifier,
    state: MatchCardState
) {
    IconCardLayout(
        modifier = modifier,
        icon = { iconModifier ->
            MatchIcon(
                modifier = iconModifier.clip(MaterialTheme.shapes.medium),
                state = state.iconState
            )
        },
        content = { contentModifier ->
            Box(
                modifier = contentModifier.fillMaxWidth()
                    .padding(Spacing.s)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    Text(
                        text = state.modeName,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing.xxs)
                    ) {
                        Text(
                            text = "${state.mapName} • ${state.time}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = "${state.xp} ${stringResource(Res.string.unit_xp)}",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }

                Column(
                    modifier = Modifier.align(Alignment.TopEnd),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs)
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