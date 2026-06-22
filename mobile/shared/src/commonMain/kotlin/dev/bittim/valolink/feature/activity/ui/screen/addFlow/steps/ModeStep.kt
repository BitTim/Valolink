/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ModeStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   17.06.26, 14:16
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.ButtonGroupStyle
import dev.bittim.valolink.core.ui.components.ConnectedButtonGroupEntry
import dev.bittim.valolink.core.ui.components.SeamlessLazyColumn
import dev.bittim.valolink.core.ui.components.SingleConnectedButtonGroup
import dev.bittim.valolink.feature.activity.ui.components.mode.ModeCard
import dev.bittim.valolink.feature.activity.ui.components.mode.ModeCardState
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowAction
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.*
import kotlin.uuid.Uuid

@Composable
fun ModeStep(
    modifier: Modifier = Modifier,
    selectedModeUuid: Uuid?,
    modeCardStates: List<ModeCardState>?,
    isRankedSelected: Boolean,
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

        Column(
            modifier = Modifier.padding(top = Spacing.m)
        ) {
            AnimatedVisibility(
                visible = modeCardStates?.firstOrNull { it.uuid == selectedModeUuid }?.canBeRanked ?: false
            ) {
                SingleConnectedButtonGroup(
                    modifier = Modifier.fillMaxWidth(),
                    entries = listOf(
                        ConnectedButtonGroupEntry(
                            label = stringResource(Res.string.activity_add_flow_mode_step_unranked),
                            icon = {},
                            weight = 1f
                        ),
                        ConnectedButtonGroupEntry(
                            label = stringResource(Res.string.activity_add_flow_mode_step_ranked),
                            icon = { Icon(imageVector = Icons.Default.MilitaryTech, contentDescription = null) },
                            weight = 1f
                        )
                    ),
                    style = ButtonGroupStyle.Tonal,
                    initialSelection = if (isRankedSelected) 1 else 0,
                    onSelectionChange = { onAction(ActivityAddFlowAction.RankedChanged(it > 0)) }
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = enableContinueButton,
                onClick = { onAction(ActivityAddFlowAction.ModeContinue) }
            ) {
                Text(text = stringResource(Res.string.generic_button_continue))
            }
        }
    }
}

@Composable
@Preview
fun ModeStepPreview() {
    MaterialTheme {
        Surface {
            val selectedUuid = Uuid.random()

            ModeStep(
                modifier = Modifier.fillMaxSize(),
                selectedModeUuid = selectedUuid,
                modeCardStates = listOf(
                    ModeCardState(
                        uuid = selectedUuid,
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
                isRankedSelected = true,
                enableContinueButton = true,
                onAction = {}
            )
        }
    }
}