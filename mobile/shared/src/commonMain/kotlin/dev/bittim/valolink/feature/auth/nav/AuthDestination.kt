/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AuthDestination.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   31.05.26, 20:33
 */

package dev.bittim.valolink.feature.auth.nav

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import dev.bittim.valolink.core.nav.UnauthenticatedDestination
import dev.bittim.valolink.core.nav.navigateBack
import dev.bittim.valolink.feature.auth.ui.screen.AuthFlowScreen
import dev.bittim.valolink.feature.auth.ui.screen.AuthFlowViewModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.koin.compose.viewmodel.koinViewModel

interface AuthDestination : UnauthenticatedDestination

val authSerializersModule = SerializersModule {
    polymorphic(NavKey::class) {
        subclass(AuthFlowNav::class)
    }
}

@Serializable
data object AuthFlowNav : AuthDestination

fun EntryProviderScope<NavKey>.authDestination(
    backStack: NavBackStack<NavKey>
) {
    entry<AuthFlowNav> {
        val authFlowViewModel = koinViewModel<AuthFlowViewModel>()
        val authFlowState by authFlowViewModel.state.collectAsStateWithLifecycle()

        AuthFlowScreen (
            state = authFlowState,
            onAction = { authFlowViewModel.onAction(it) },
            navBack = { backStack.navigateBack() }
        )
    }
}