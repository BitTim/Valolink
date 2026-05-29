/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AuthDestination.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   29.05.26, 15:52
 */

package dev.bittim.valolink.feature.auth.nav

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import dev.bittim.valolink.core.nav.UnauthenticatedDestination
import dev.bittim.valolink.core.nav.navigateBack
import dev.bittim.valolink.core.nav.navigateTo
import dev.bittim.valolink.feature.auth.ui.screen.email.EmailScreen
import dev.bittim.valolink.feature.auth.ui.screen.email.EmailScreenViewModel
import dev.bittim.valolink.feature.auth.ui.screen.landing.LandingScreen
import dev.bittim.valolink.feature.auth.ui.screen.password.PasswordScreen
import dev.bittim.valolink.feature.auth.ui.screen.password.PasswordScreenViewModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.koin.compose.viewmodel.koinViewModel

interface AuthDestination : UnauthenticatedDestination

val authSerializersModule = SerializersModule {
    polymorphic(NavKey::class) {
        subclass(LandingScreenNav::class)
    }
}

@Serializable
data object LandingScreenNav : AuthDestination

@Serializable
data object EmailScreenNav : AuthDestination

@Serializable
data object PasswordScreenNav : AuthDestination

fun EntryProviderScope<NavKey>.authDestination(
    backStack: NavBackStack<NavKey>
) {
    entry<LandingScreenNav> {
        LandingScreen(navEmail = { backStack.navigateTo(EmailScreenNav) })
    }

    entry<EmailScreenNav> {
        val emailScreenViewModel = koinViewModel<EmailScreenViewModel>()
        val emailScreenState by emailScreenViewModel.state.collectAsStateWithLifecycle()

        EmailScreen(
            state = emailScreenState,
            validateEmail = { emailScreenViewModel.validateEmail(it) },
            onNext = { emailScreenViewModel.onNext(email = it, navNext = { backStack.navigateTo(PasswordScreenNav) })},
            navBack = { backStack.navigateBack() }
        )
    }

    entry<PasswordScreenNav> {
        val passwordScreenViewModel = koinViewModel<PasswordScreenViewModel>()
        val passwordScreenState by passwordScreenViewModel.state.collectAsStateWithLifecycle()

        PasswordScreen(
            state = passwordScreenState,
            validatePassword = { passwordScreenViewModel.validatePassword(it) },
            navBack = { backStack.navigateBack() },
            navNext = { }
        )
    }
}