/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityListScreen.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   26.09.26, 17:00
 */

package dev.bittim.valolink.feature.activity.ui.screen.list

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LifecycleResumeEffect
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.feature.activity.ui.screen.list.composables.ActivityListContentScreen
import dev.bittim.valolink.feature.activity.ui.screen.list.composables.ActivityListEmptyScreen
import dev.bittim.valolink.feature.activity.ui.screen.list.composables.ActivityListErrorScreen
import dev.bittim.valolink.feature.activity.ui.screen.list.composables.ActivityListLoadingScreen
import dev.bittim.valolink.feature.activity.ui.screen.list.state.ActivityListState

@Composable
fun ActivityListScreen(
    state: ActivityListState,
    onAction: (ActivityListAction) -> Unit
) {
    LifecycleResumeEffect(Unit) {
        onAction(ActivityListAction.Refresh)
        onPauseOrDispose {  }
    }

    Surface(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.displayCutout)
            .windowInsetsPadding(WindowInsets.systemBars)
            .windowInsetsPadding(WindowInsets.ime)
    ) {
        Crossfade(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = Spacing.l),
            targetState = state
        ) {
            when (it) {
                is ActivityListState.Content -> {
                    ActivityListContentScreen(
                        modifier = Modifier.fillMaxSize(),
                        items = it.items,
                        onAction = onAction
                    )
                }
                ActivityListState.Empty -> ActivityListEmptyScreen(
                    modifier = Modifier.fillMaxSize()
                )
                ActivityListState.Error -> ActivityListErrorScreen(
                    modifier = Modifier.fillMaxSize()
                )
                ActivityListState.Loading -> ActivityListLoadingScreen(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}