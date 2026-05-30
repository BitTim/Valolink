/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       UnauthenticatedNavRoot.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.05.26, 02:46
 */

package dev.bittim.valolink.core.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import dev.bittim.valolink.feature.auth.nav.LandingScreenNav
import dev.bittim.valolink.feature.auth.nav.authDestination
import org.koin.compose.getKoin
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.qualifier.named

@OptIn(KoinExperimentalAPI::class)
@Composable
fun UnauthenticatedNavRoot() {
    val backStack = rememberNavBackStack(SavedStateConfiguration {
        serializersModule = unauthenticatedSerializersModule
    }, LandingScreenNav)
    val current = backStack.last()

    val koin = getKoin()
    val authFlowScope = remember { koin.getOrCreateScope("authFlowScopeId", named("AuthFlowScope")) }

    DisposableEffect(Unit) {
        onDispose {
            authFlowScope.close()
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.navigateBack() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            authDestination(backStack, authFlowScope)
        }
    )
}