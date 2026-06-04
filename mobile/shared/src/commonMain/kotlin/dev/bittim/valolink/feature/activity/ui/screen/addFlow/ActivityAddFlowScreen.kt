/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowScreen.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   04.06.26, 12:17
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow

import androidx.compose.foundation.layout.*
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.flowScaffold.FlowScaffold

@Composable
@Preview
fun ActivityAddFlowScreen(
    state: ActivityAddFlowState = ActivityAddFlowState(),
    onAction: (ActivityAddFlowAction) -> Unit = {},
    navBack: () -> Unit = {},
) {
    FlowScaffold(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.displayCutout)
            .windowInsetsPadding(WindowInsets.systemBars)
            .windowInsetsPadding(WindowInsets.ime),
        padding = PaddingValues(horizontal = Spacing.l),
        step = state.step,
        cancellable = false,
        onBack = { onAction(ActivityAddFlowAction.Back(navBack)) },
        menuContent = {
            DropdownMenuItem(
                leadingIcon = {},
                text = {},
                onClick = {}
            )
        },
        hero = {},
        content = { _, padding ->

        }
    )
}