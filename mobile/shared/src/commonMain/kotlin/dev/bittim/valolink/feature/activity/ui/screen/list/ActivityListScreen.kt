/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityListScreen.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 20:36
 */

package dev.bittim.valolink.feature.activity.ui.screen.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LifecycleResumeEffect
import dev.bittim.valolink.core.domain.model.*
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.SeamlessLazyColumn
import dev.bittim.valolink.feature.activity.ui.components.match.MatchCard
import dev.bittim.valolink.feature.activity.ui.components.match.MatchCardState
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.*
import kotlin.time.Clock
import kotlin.uuid.Uuid

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
            SeamlessLazyColumn (
                modifier = Modifier.weight(1f),
            ) {
                items(state.activities ?: emptyList()) { activity ->
                    when(activity) {
                        is Activity.MatchActivity -> {
                            MatchCard(
                                modifier = Modifier.fillMaxWidth(),
                                state = MatchCardState.fromActivity(activity)
                            )
                        }
                        is Activity.RrRefundActivity -> Text(
                            text = "${activity.rr} ${stringResource(Res.string.unit_rr)} ${stringResource(Res.string.rr_refund_label)} - ${activity.mode.displayName}"
                        )
                        is Activity.XpCorrectionActivity -> Text(
                            text = "${activity.xp} ${stringResource(Res.string.unit_xp)} ${stringResource(Res.string.xp_correction_label)}"
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun ActivityListScreenPreview() {
    MaterialTheme {
        Surface {
            val map = SimpleValoMap(
                uuid = Uuid.random(),
                displayName = "Bind",
                coordinates = "Who Cares",
                category = ValoMapCategory.Standard,
                listViewIcon = "",
                listViewIconTall = "",
                splash = "",
                premierBackgroundImage = "",
                stylizedBackgroundImage = ""
            )

            val mode = ValoMode(
                uuid = Uuid.random(),
                displayName = "Standard",
                description = "",
                duration = "",
                category = ValoModeCategory.Standard,
                displayIcon = "",
                listViewIconTall = "",
                roundsPerHalf = 0,
                canBeRanked = true
            )

            ActivityListScreen(
                state = ActivityListState(
                    activities = listOf(
                        Activity.MatchActivity(
                            id = Uuid.random(),
                            userId = Uuid.random(),
                            time = Clock.System.now(),
                            xp = 1234,
                            rr = 23,
                            matchParticipant = MatchParticipant(
                                userId = Uuid.random(),
                                visibleRr = 23,
                                isOwner = true,
                                isTeamB = false
                            ),
                            match = Match(
                                id = Uuid.random(),
                                scoreA = 6,
                                scoreB = 2,
                                endReason = MatchEndReason.SURRENDER_B,
                                isRanked = true,
                                time = Clock.System.now(),
                                map = map,
                                mode = mode,
                            )
                        ),
                        Activity.MatchActivity(
                            id = Uuid.random(),
                            userId = Uuid.random(),
                            time = Clock.System.now(),
                            xp = 7639,
                            rr = 0,
                            matchParticipant = MatchParticipant(
                                userId = Uuid.random(),
                                visibleRr = 0,
                                isOwner = true,
                                isTeamB = false
                            ),
                            match = Match(
                                id = Uuid.random(),
                                scoreA = 18,
                                scoreB = 18,
                                endReason = MatchEndReason.COMPLETED,
                                isRanked = true,
                                time = Clock.System.now(),
                                map = map,
                                mode = mode,
                            )
                        ),
                        Activity.MatchActivity(
                            id = Uuid.random(),
                            userId = Uuid.random(),
                            time = Clock.System.now(),
                            xp = 947,
                            rr = null,
                            matchParticipant = MatchParticipant(
                                userId = Uuid.random(),
                                visibleRr = null,
                                isOwner = true,
                                isTeamB = false
                            ),
                            match = Match(
                                id = Uuid.random(),
                                scoreA = 7,
                                scoreB = 13,
                                endReason = MatchEndReason.COMPLETED,
                                isRanked = false,
                                time = Clock.System.now(),
                                map = map,
                                mode = mode,
                            )
                        ),
                    )
                ),
                onAction = {}
            )
        }
    }
}