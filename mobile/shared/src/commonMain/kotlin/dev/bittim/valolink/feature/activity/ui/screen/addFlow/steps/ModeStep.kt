/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ModeStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   25.08.26, 14:05
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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

/**
 * Displays the activity mode selection step.
 *
 * @param selectedModeUuid The UUID of the currently selected mode.
 * @param modeCardStates The available mode cards, or `null` when no modes are available.
 * @param isRankedSelected Whether ranked mode is initially selected.
 * @param enableContinueButton Whether the Continue button is enabled.
 * @param onAction Receives mode selection, ranking selection, and Continue actions.
 */
/**
 * Displays the mode selection step for the activity add flow.
 *
 * @param selectedModeUuid The UUID of the currently selected mode.
 * @param modeCardStates The available mode cards, or `null` when no modes are available.
 * @param isRankedSelected Whether ranked mode is initially selected.
 * @param enableContinueButton Whether the Continue button is enabled.
 * @param enableRrRefundOption Whether the RR refund menu option is enabled.
 * @param onAction Handles mode selection, ranking changes, navigation, and continuation actions.
 */
@Composable
fun ModeStep(
    modifier: Modifier = Modifier,
    selectedModeUuid: Uuid?,
    modeCardStates: List<ModeCardState>?,
    isRankedSelected: Boolean,
    enableContinueButton: Boolean,
    enableRrRefundOption: Boolean,
    onAction: (ActivityAddFlowAction) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing.l)
    ) {
        Text(
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

            Row {
                Button(
                    modifier = Modifier.weight(1f),
                    enabled = enableContinueButton,
                    shape = RoundedCornerShape(
                        topStart = Spacing.xl,
                        topEnd = Spacing.s,
                        bottomStart = Spacing.xl,
                        bottomEnd = Spacing.s
                    ),
                    onClick = { onAction(ActivityAddFlowAction.ModeContinue) }
                ) {
                    Text(text = stringResource(Res.string.generic_button_continue))
                }

                Box {
                    FilledIconButton(
                        onClick = { menuExpanded = !menuExpanded },
                        shape = RoundedCornerShape(
                            topStart = Spacing.s,
                            topEnd = Spacing.xl,
                            bottomStart = Spacing.s,
                            bottomEnd = Spacing.xl
                        ),
                    ) {
                        val iconRotation = animateFloatAsState(if (menuExpanded) 180f else 0f)

                        Icon(
                            modifier = Modifier.rotate(iconRotation.value),
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                            },
                            text = {
                                Text(text = stringResource(Res.string.activity_add_flow_xp_correction_menu_item))
                            },
                            onClick = {
                                onAction(ActivityAddFlowAction.ToXpCorrection)
                                menuExpanded = false
                            }
                        )

                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Replay, contentDescription = null)
                            },
                            text = {
                                Text(text = stringResource(Res.string.activity_add_flow_rr_refund_menu_item))
                            },
                            enabled = enableRrRefundOption,
                            onClick = {
                                onAction(ActivityAddFlowAction.ToRrRefund)
                                menuExpanded = false
                            }
                        )
                    }
                }
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
                enableRrRefundOption = true,
                onAction = {}
            )
        }
    }
}