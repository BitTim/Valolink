/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MapStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   15.06.26, 20:51
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
import dev.bittim.valolink.feature.activity.ui.components.map.MapCard
import dev.bittim.valolink.feature.activity.ui.components.map.MapCardState
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowAction
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.activity_add_flow_map_step_title
import valolink.shared.generated.resources.generic_button_continue
import kotlin.uuid.Uuid

@Composable
fun MapStep(
    modifier: Modifier = Modifier,
    selectedMapUuid: Uuid?,
    mapCardStates: List<MapCardState>?,
    onAction: (ActivityAddFlowAction) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier.padding(bottom = Spacing.m),
            text = stringResource(Res.string.activity_add_flow_map_step_title),
            style = MaterialTheme.typography.titleLarge
        )

        SeamlessLazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(mapCardStates ?: emptyList()) { mapCardState ->
                MapCard(
                    modifier = Modifier.fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .border(
                            width = Spacing.xxs,
                            color = if (mapCardState.uuid == selectedMapUuid) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = MaterialTheme.shapes.medium
                        )
                        .clickable { onAction(ActivityAddFlowAction.MapSelected(mapCardState.uuid)) },
                    state = mapCardState
                )
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth()
                .padding(top = Spacing.m),
            enabled = selectedMapUuid != null,
            onClick = { onAction(ActivityAddFlowAction.MapContinue) }
        ) {
            Text(text = stringResource(Res.string.generic_button_continue))
        }
    }
}

@Composable
@Preview
fun MapStepPreview() {
    MaterialTheme {
        Surface {
            MapStep(
                modifier = Modifier.fillMaxSize(),
                selectedMapUuid = Uuid.random(),
                mapCardStates = listOf(
                    MapCardState(
                        uuid = Uuid.random(),
                        title = "Sample Map",
                        coordinates = "123.4567E, 89.1234S",
                        imageUrl = ""
                    ),
                    MapCardState(
                        uuid = Uuid.random(),
                        title = "Sample Map",
                        coordinates = "123.4567E, 89.1234S",
                        imageUrl = ""
                    ),
                    MapCardState(
                        uuid = Uuid.random(),
                        title = "Sample Map",
                        coordinates = "123.4567E, 89.1234S",
                        imageUrl = ""
                    )
                ),
                onAction = {}
            )
        }
    }
}