/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       App.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 17:47
 */

package dev.bittim.valolink

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.bittim.valolink.core.ui.screen.root.RootScreen
import dev.bittim.valolink.core.ui.screen.root.RootScreenViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        val rootViewModel = koinViewModel<RootScreenViewModel>()
        val rootState by rootViewModel.state.collectAsStateWithLifecycle()

        RootScreen(rootState)
    }
}