/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityListScreen.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   22.06.26, 17:12
 */

package dev.bittim.valolink.feature.activity.ui.screen.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.domain.extension.toLocalizedString
import dev.bittim.valolink.core.domain.model.MatchOutcome
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.feature.activity.ui.components.match.*
import kotlin.time.Clock

@Composable
@Preview
fun ActivityListScreen(
    state: ActivityListState = ActivityListState()
) {
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
            val mapImageUrl = "https://media.valorant-api.com/maps/2c9d57ec-4431-9c5e-2939-8f9ef6dd5cba/splash.png"
            val modeIconUrl = "https://media.valorant-api.com/gamemodes/96bd3920-4f36-d026-2b28-c683eb0bcac5/displayicon.png"
            val rankUrl = "https://media.valorant-api.com/competitivetiers/03621f52-342b-cf4e-4f86-9350a49c6d04/13/largeicon.png"

            MatchCard(
                modifier = Modifier.fillMaxWidth(),
                state = MatchCardState(
                    iconState = MatchIconState(
                        outcome = MatchOutcome.Win,
                        mapImageUrl = mapImageUrl,
                        iconUrl = rankUrl,
                        rrChipState = RrChipState(
                            rr = 23,
                            rankChanged = true
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
                    xp = 1000
                )
            )

            MatchCard(
                modifier = Modifier.fillMaxWidth(),
                state = MatchCardState(
                    iconState = MatchIconState(
                        outcome = MatchOutcome.Draw,
                        mapImageUrl = mapImageUrl,
                        iconUrl = rankUrl,
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
                    xp = 1000
                )
            )

            MatchCard(
                modifier = Modifier.fillMaxWidth(),
                state = MatchCardState(
                    iconState = MatchIconState(
                        outcome = MatchOutcome.Loss,
                        mapImageUrl = mapImageUrl,
                        iconUrl = modeIconUrl,
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
                    xp = 1000
                )
            )

            Text(
                text = "Actual data"
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.activities ?: emptyList()) { activity ->
                    Text(
                        text = activity.type.name
                    )
                }
            }
        }
    }
}