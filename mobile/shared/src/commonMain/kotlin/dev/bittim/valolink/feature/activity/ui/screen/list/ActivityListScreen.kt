/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityListScreen.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   04.06.26, 12:17
 */

package dev.bittim.valolink.feature.activity.ui.screen.list

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.domain.extension.toLocalizedString
import dev.bittim.valolink.core.domain.model.MatchOutcome
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.feature.activity.ui.components.match.MatchCard
import dev.bittim.valolink.feature.activity.ui.components.match.MatchCardState
import dev.bittim.valolink.feature.activity.ui.components.match.MatchIconState
import dev.bittim.valolink.feature.activity.ui.components.match.ScoreChipState
import kotlin.time.Clock

@Composable
@Preview
fun ActivityListScreen() {
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
            verticalArrangement = Arrangement.spacedBy(Spacing.m)
        ) {
            Text(
                "Activity",
                style = MaterialTheme.typography.headlineLarge
            )

            val outcome = MatchOutcome.Win
            val mapImageUrl = "https://media.valorant-api.com/maps/2c9d57ec-4431-9c5e-2939-8f9ef6dd5cba/splash.png"
            val modeIconUrl = "https://media.valorant-api.com/gamemodes/96bd3920-4f36-d026-2b28-c683eb0bcac5/displayicon.png"

            MatchCard(
                modifier = Modifier.fillMaxWidth(),
                state = MatchCardState(
                    iconState = MatchIconState(
                        outcome = outcome,
                        mapImageUrl = mapImageUrl,
                        modeIconUrl = modeIconUrl
                    ),
                    scoreChipState = ScoreChipState(
                        outcome = outcome,
                        wasSurrender = true,
                        score = "6 - 2"
                    ),
                    modeName = "Standard",
                    mapName = "Bind",
                    time = Clock.System.now().toLocalizedString(),
                    xp = 1000
                )
            )
        }
    }
}