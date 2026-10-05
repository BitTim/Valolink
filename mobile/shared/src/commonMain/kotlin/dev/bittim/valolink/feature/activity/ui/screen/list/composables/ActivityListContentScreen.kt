/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityListContentScreen.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   05.10.26, 03:05
 */

package dev.bittim.valolink.feature.activity.ui.screen.list.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.domain.extension.toLocalizedString
import dev.bittim.valolink.core.domain.model.MatchOutcome
import dev.bittim.valolink.core.ui.components.SeamlessLazyColumn
import dev.bittim.valolink.feature.activity.ui.components.match.*
import dev.bittim.valolink.feature.activity.ui.screen.list.ActivityListAction
import dev.bittim.valolink.feature.activity.ui.screen.list.state.ActivityListItemState
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.*
import kotlin.time.Clock
import kotlin.uuid.Uuid

@Composable
fun ActivityListContentScreen(
    modifier: Modifier = Modifier,
    items: List<ActivityListItemState> = emptyList(),
    onAction: (ActivityListAction) -> Unit
) {
    SeamlessLazyColumn(
        modifier = modifier,
    ) {
        items(items = items, key = { it.id }) { item ->
            when (item) {
                is ActivityListItemState.MatchCard -> {
                    MatchCard(
                        modifier = Modifier.fillMaxWidth(),
                        state = item.state
                    )
                }

                is ActivityListItemState.RrRefund -> Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "${item.rr} ${stringResource(Res.string.unit_rr)} ${stringResource(Res.string.rr_refund_label)} - ${item.modeName}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                is ActivityListItemState.XpCorrection -> Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "${item.xp} ${stringResource(Res.string.unit_xp)} ${stringResource(Res.string.xp_correction_label)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
@Preview
fun ActivityListContainerPopulatedPreviewScreen() {
    MaterialTheme {
        Surface {
            ActivityListContentScreen(
                items = listOf(
                    ActivityListItemState.MatchCard(
                        id = Uuid.random(),
                        state = MatchCardState(
                            iconState = MatchIconState(
                                outcome = MatchOutcome.Win,
                                mapImageUrl = "",
                                iconUrl = "",
                                rrChipState = RrChipState(
                                    rr = 23,
                                    rankChanged = false
                                )
                            ),
                            scoreChipState = ScoreChipState(
                                outcome = MatchOutcome.Win,
                                wasSurrender = true,
                                score = "6 - 2"
                            ),
                            modeName = "Standard",
                            mapName = "Bind",
                            time = Clock.System.now().toLocalizedString(),
                            xp = 1234
                        )
                    ),
                    ActivityListItemState.MatchCard(
                        id = Uuid.random(),
                        state = MatchCardState(
                            iconState = MatchIconState(
                                outcome = MatchOutcome.Draw,
                                mapImageUrl = "",
                                iconUrl = "",
                                rrChipState = RrChipState(
                                    rr = 0,
                                    rankChanged = false
                                )
                            ),
                            scoreChipState = ScoreChipState(
                                outcome = MatchOutcome.Draw,
                                wasSurrender = false,
                                score = "18 - 18"
                            ),
                            modeName = "Standard",
                            mapName = "Bind",
                            time = Clock.System.now().toLocalizedString(),
                            xp = 7639
                        )
                    ),
                    ActivityListItemState.MatchCard(
                        id = Uuid.random(),
                        state = MatchCardState(
                            iconState = MatchIconState(
                                outcome = MatchOutcome.Loss,
                                mapImageUrl = "",
                                iconUrl = "",
                                rrChipState = null
                            ),
                            scoreChipState = ScoreChipState(
                                outcome = MatchOutcome.Loss,
                                wasSurrender = false,
                                score = "7 - 13"
                            ),
                            modeName = "Standard",
                            mapName = "Bind",
                            time = Clock.System.now().toLocalizedString(),
                            xp = 947
                        )
                    )
                ),
                onAction = {}
            )
        }
    }
}