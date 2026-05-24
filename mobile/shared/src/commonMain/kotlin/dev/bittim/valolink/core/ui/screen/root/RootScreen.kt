/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       RootScreen.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 18:02
 */

package dev.bittim.valolink.core.ui.screen.root

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.domain.model.AuthState
import dev.bittim.valolink.core.nav.AuthenticatedNavRoot
import dev.bittim.valolink.core.nav.UnauthenticatedNavRoot
import dev.bittim.valolink.core.ui.screen.splash.SplashScreen

@Composable
@Preview
fun RootScreen(
    state: RootScreenState = RootScreenState()
) {
    AnimatedContent(targetState = state.authState) { authState ->
        when (authState) {
            is AuthState.Loading -> SplashScreen()
            is AuthState.Authenticated -> AuthenticatedNavRoot()
            is AuthState.Unauthenticated -> UnauthenticatedNavRoot()
        }
    }
}