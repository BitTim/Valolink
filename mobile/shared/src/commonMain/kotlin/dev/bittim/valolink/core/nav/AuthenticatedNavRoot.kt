/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AuthenticatedNavRoot.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   04.06.26, 01:18
 */

package dev.bittim.valolink.core.nav

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import dev.bittim.valolink.core.ui.components.AppScaffold
import dev.bittim.valolink.feature.activity.nav.activityDestination
import dev.bittim.valolink.feature.home.nav.HomeScreenNav
import dev.bittim.valolink.feature.home.nav.homeDestination
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AuthenticatedNavRoot() {
    val backStack = rememberNavBackStack(SavedStateConfiguration {
        serializersModule = authenticatedSerializersModule
    }, HomeScreenNav)
    val current = backStack.last()

    AppScaffold(
        showNavBar = current is AuthenticatedDestination && current.showBottomNav,
        onNavigateTopLevel = { destination ->
            backStack.navigateToTopLevel(destination)
        }
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.navigateBack() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                homeDestination(backStack)
                activityDestination(backStack)
            }
        )
    }
}