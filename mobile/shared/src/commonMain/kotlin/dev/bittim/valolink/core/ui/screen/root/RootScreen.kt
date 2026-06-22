/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       RootScreen.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   06.06.26, 23:32
 */

package dev.bittim.valolink.core.ui.screen.root

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.data.sync.SyncManager
import dev.bittim.valolink.core.domain.model.AuthState
import dev.bittim.valolink.core.nav.AuthenticatedNavRoot
import dev.bittim.valolink.core.nav.UnauthenticatedNavRoot
import dev.bittim.valolink.core.ui.ColorSchemeExtension
import dev.bittim.valolink.core.ui.LocalColorSchemeExtension
import dev.bittim.valolink.core.ui.screen.splash.SplashScreen
import org.koin.compose.koinInject

@Composable
@Preview
fun RootScreen(
    state: RootScreenState = RootScreenState()
) {
    val syncManager = koinInject<SyncManager>()

    CompositionLocalProvider(
        LocalColorSchemeExtension provides ColorSchemeExtension()
    ) {
        AnimatedContent(targetState = state.authState) { authState ->
            when (authState) {
                is AuthState.Loading -> SplashScreen()
                is AuthState.Authenticated -> {
                    syncManager.init()
                    AuthenticatedNavRoot()
                }
                is AuthState.Unauthenticated -> {
                    syncManager.stop()
                    UnauthenticatedNavRoot()
                }
            }
        }
    }
}