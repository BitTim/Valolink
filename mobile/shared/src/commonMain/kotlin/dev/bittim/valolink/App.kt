/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       App.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 12:57
 */

package dev.bittim.valolink

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import dev.bittim.valolink.core.di.appModule
import dev.bittim.valolink.core.nav.AppDestination
import dev.bittim.valolink.core.nav.NavRoot
import dev.bittim.valolink.core.nav.appSerializersModule
import dev.bittim.valolink.core.nav.navigateToTopLevel
import dev.bittim.valolink.core.ui.screen.RootContainerScreen
import dev.bittim.valolink.feature.home.di.homeModule
import dev.bittim.valolink.feature.home.nav.HomeScreenNav
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration

@Composable
@Preview
fun App() {
    val backStack = rememberNavBackStack(SavedStateConfiguration {
        serializersModule = appSerializersModule
    }, HomeScreenNav)
    val showNavBar = backStack.lastOrNull()?.let { it is AppDestination && it.showBottomNav } == true

    KoinApplication(configuration = koinConfiguration {
        modules(
            appModule,
            homeModule,
        )
    }) {
        MaterialTheme {
            RootContainerScreen(
                showNavBar = showNavBar,
                navigateTopLevel = { backStack.navigateToTopLevel(it) },
                content = {
                    NavRoot(backStack = backStack)
                }
            )
        }
    }
}