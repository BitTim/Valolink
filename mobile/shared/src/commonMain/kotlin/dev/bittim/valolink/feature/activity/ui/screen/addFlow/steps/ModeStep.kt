/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ModeStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   10.06.26, 01:04
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.domain.model.ValoMode
import dev.bittim.valolink.core.domain.model.ValoModeCategory
import dev.bittim.valolink.core.ui.Spacing
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
    modes: List<ValoMode>?,
    onAction: (ActivityAddFlowAction) -> Unit
) {
    val lazyListState = rememberLazyListState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier.padding(bottom = Spacing.m),
            text = stringResource(Res.string.activity_add_flow_mode_step_title),
            style = MaterialTheme.typography.titleLarge
        )

        Box(
            modifier = Modifier.weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState,
                contentPadding = PaddingValues(bottom = Spacing.xxl),
                verticalArrangement = Arrangement.spacedBy(Spacing.s)
            ) {
                items(modes ?: emptyList()) { mode ->
                    ModeCard(
                        modifier = Modifier.fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .border(
                                width = Spacing.xxs,
                                color = if (mode.uuid == selectedModeUuid) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = MaterialTheme.shapes.medium
                            )
                            .clickable { onAction(ActivityAddFlowAction.ModeSelected(mode)) },
                        state = ModeCardState.from(mode)
                    )
                }
            }

            this@Column.AnimatedVisibility(
                modifier = Modifier.align(Alignment.TopCenter),
                visible = lazyListState.canScrollBackward
            ) {
                Box(
                    modifier = Modifier.aspectRatio(5f/1f)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.surface,
                                    Color.Transparent
                                )
                            )
                        )
                )
            }

            this@Column.AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomCenter),
                visible = lazyListState.canScrollForward
            ) {
                Box(
                    modifier = Modifier.aspectRatio(5f/1f)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.surface
                                )
                            )
                        )
                )
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth()
                .padding(top = Spacing.m),
            enabled = selectedModeUuid != null,
            onClick = { onAction(ActivityAddFlowAction.ModeProceed) }
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
                modes = listOf(
                    ValoMode(
                        uuid = Uuid.random(),
                        displayName = "Sample Mode",
                        description = "",
                        duration = "10-15 MINS",
                        category = ValoModeCategory.Standard,
                        displayIcon = "",
                        listViewIconTall = "",
                        roundsPerHalf = 0,
                        canBeRanked = true
                    ),
                    ValoMode(
                        uuid = Uuid.random(),
                        displayName = "Sample Mode",
                        description = "",
                        duration = "10-15 MINS",
                        category = ValoModeCategory.Standard,
                        displayIcon = "",
                        listViewIconTall = "",
                        roundsPerHalf = 0,
                        canBeRanked = true
                    ),
                    ValoMode(
                        uuid = Uuid.random(),
                        displayName = "Sample Mode",
                        description = "",
                        duration = "10-15 MINS",
                        category = ValoModeCategory.Standard,
                        displayIcon = "",
                        listViewIconTall = "",
                        roundsPerHalf = 0,
                        canBeRanked = true
                    ),
                ),
                onAction = {}
            )
        }
    }
}