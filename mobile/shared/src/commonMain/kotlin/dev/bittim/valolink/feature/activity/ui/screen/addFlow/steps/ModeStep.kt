/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ModeStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.06.26, 20:49
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps

import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.feature.activity.ui.components.mode.ModeCard
import dev.bittim.valolink.feature.activity.ui.components.mode.ModeCardState
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowAction
import kotlin.uuid.Uuid

@Composable
fun ModeStep(
    modifier: Modifier = Modifier,
    selectedModeUuid: Uuid?,
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
                    .clip(MaterialTheme.shapes.medium)
                    .border(
                        width = Spacing.xxs,
                        color = if(modeState.uuid == selectedModeUuid) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = MaterialTheme.shapes.medium)
                    .clickable { onAction(ActivityAddFlowAction.ModeSelected(modeState)) },
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
                selectedModeUuid = Uuid.random(),
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