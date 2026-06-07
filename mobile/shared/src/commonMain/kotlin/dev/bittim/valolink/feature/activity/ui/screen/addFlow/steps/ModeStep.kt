/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ModeStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.06.26, 20:21
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.feature.activity.ui.components.mode.ModeCard
import dev.bittim.valolink.feature.activity.ui.components.mode.ModeCardState
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowAction
import kotlin.uuid.Uuid

@Composable
fun ModeStep(
    modifier: Modifier = Modifier,
    modeStates: List<ModeCardState>?,
    onAction: (ActivityAddFlowAction) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing.s)
    ) {
        items(modeStates ?: emptyList()) { modeState ->
            ModeCard(
                modifier = Modifier.fillMaxWidth()
                    .clickable { onAction(ActivityAddFlowAction.ModeSelected(modeState.uuid)) },
                state = modeState
            )
        }
    }
}

@Composable
@Preview
fun ModeStepPreview() {
    MaterialTheme {
        Surface {
            ModeStep(
                modifier = Modifier.fillMaxSize(),
                modeStates = listOf(
                    ModeCardState(
                        uuid = Uuid.random(),
                        iconUrl = "",
                        title = "Sample Mode",
                        duration = "10-50 MINS",
                        canBeRanked = true
                    ),
                    ModeCardState(
                        uuid = Uuid.random(),
                        iconUrl = "",
                        title = "Sample Mode",
                        duration = "10-50 MINS",
                        canBeRanked = true
                    ),
                    ModeCardState(
                        uuid = Uuid.random(),
                        iconUrl = "",
                        title = "Sample Mode",
                        duration = "10-50 MINS",
                        canBeRanked = true
                    )
                ),
                onAction = {}
            )
        }
    }
}