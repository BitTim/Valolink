/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       UnauthenticatedNavRoot.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 17:48
 */

package dev.bittim.valolink.core.nav

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import dev.bittim.valolink.feature.auth.nav.LandingScreenNav
import dev.bittim.valolink.feature.auth.nav.authDestination
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun UnauthenticatedNavRoot() {
    val backStack = rememberNavBackStack(SavedStateConfiguration {
        serializersModule = unauthenticatedSerializersModule
    }, LandingScreenNav)
    val current = backStack.last()

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.navigateBack() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            authDestination(backStack)
        }
    )
}