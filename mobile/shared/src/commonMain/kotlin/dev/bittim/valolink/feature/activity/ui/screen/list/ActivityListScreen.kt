/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityListScreen.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.08.26, 18:29
 */

package dev.bittim.valolink.feature.activity.ui.screen.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LifecycleResumeEffect
import dev.bittim.valolink.core.domain.extension.toLocalizedString
import dev.bittim.valolink.core.domain.model.MatchOutcome
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.SeamlessLazyColumn
import dev.bittim.valolink.feature.activity.ui.components.match.*
import dev.bittim.valolink.feature.activity.ui.screen.list.state.ActivityListItemState
import dev.bittim.valolink.feature.activity.ui.screen.list.state.ActivityListState
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.*
import kotlin.time.Clock

@Composable
fun ActivityListScreen(
    state: ActivityListState = ActivityListState(),
    onAction: (ActivityListAction) -> Unit
) {
    LifecycleResumeEffect(Unit) {
        onAction(ActivityListAction.Refresh)
        onPauseOrDispose { }
    }

    Surface(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.displayCutout)
            .windowInsetsPadding(WindowInsets.systemBars)
            .windowInsetsPadding(WindowInsets.ime)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = Spacing.l),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.s)
        ) {
            AnimatedContent(
                targetState = state.items?.isEmpty() == true
            ) {
                if (it) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier.size(Spacing.xxl),
                            imageVector = Icons.AutoMirrored.Filled.EventNote,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outlineVariant
                        )

                        Spacer(modifier = Modifier.height(Spacing.l))

                        Text(
                            text = stringResource(Res.string.activity_list_empty),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            modifier = Modifier.padding(horizontal = Spacing.xxxl),
                            text = stringResource(Res.string.activity_list_empty_subtext),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    SeamlessLazyColumn(
                        modifier = Modifier.weight(1f),
                    ) {
                        items(state.items ?: emptyList()) { item ->
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
            }
        }
    }
}

@Composable
@Preview
fun ActivityListScreenEmptyPreview() {
    MaterialTheme {
        Surface {
            ActivityListScreen(
                state = ActivityListState(items = listOf()),
                onAction = {}
            )
        }
    }
}

@Composable
@Preview
fun ActivityListScreenPopulatedPreview() {
    MaterialTheme {
        Surface {
            ActivityListScreen(
                state = ActivityListState(
                    items = listOf(
                        ActivityListItemState.MatchCard(
                            MatchCardState(
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
                            MatchCardState(
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
                            MatchCardState(
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
                    )
                ),
                onAction = {}
            )
        }
    }
}