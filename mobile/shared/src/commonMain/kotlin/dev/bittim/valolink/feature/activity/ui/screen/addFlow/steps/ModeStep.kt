/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ModeStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   16.06.26, 02:16
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.SeamlessLazyColumn
import dev.bittim.valolink.feature.activity.ui.components.mode.ModeCard
import dev.bittim.valolink.feature.activity.ui.components.mode.ModeCardState
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowAction
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.activity_add_flow_mode_step_title
import valolink.shared.generated.resources.generic_button_continue
import kotlin.uuid.Uuid

@Composable
fun ModeStep(
    modifier: Modifier = Modifier,
    selectedModeUuid: Uuid?,
    modeCardStates: List<ModeCardState>?,
    enableContinueButton: Boolean,
    onAction: (ActivityAddFlowAction) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier.padding(bottom = Spacing.m),
            text = stringResource(Res.string.activity_add_flow_mode_step_title),
            style = MaterialTheme.typography.titleLarge
        )

        SeamlessLazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(modeCardStates ?: emptyList()) { modeCardState ->
                ModeCard(
                    modifier = Modifier.fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .border(
                            width = Spacing.xxs,
                            color = if (modeCardState.uuid == selectedModeUuid) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = MaterialTheme.shapes.medium
                        )
                        .clickable { onAction(ActivityAddFlowAction.ModeSelected(modeCardState.uuid)) },
                    state = modeCardState
                )
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth()
                .padding(top = Spacing.m),
            enabled = enableContinueButton,
            onClick = { onAction(ActivityAddFlowAction.ModeContinue) }
        ) {
            Text(text = stringResource(Res.string.generic_button_continue))
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
                modeCardStates = listOf(
                    ModeCardState(
                        uuid = Uuid.random(),
                        iconUrl = "",
                        title = "Sample Mode",
                        duration = "10-15 MIN",
                        canBeRanked = true
                    ),
                    ModeCardState(
                        uuid = Uuid.random(),
                        iconUrl = "",
                        title = "Sample Mode",
                        duration = "10-15 MIN",
                        canBeRanked = false
                    ),
                    ModeCardState(
                        uuid = Uuid.random(),
                        iconUrl = "",
                        title = "Sample Mode",
                        duration = "10-15 MIN",
                        canBeRanked = true
                    )
                ),
                enableContinueButton = true,
                onAction = {}
            )
        }
    }
}