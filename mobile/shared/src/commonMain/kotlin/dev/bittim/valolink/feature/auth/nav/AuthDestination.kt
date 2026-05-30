/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AuthDestination.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.05.26, 02:46
 */

package dev.bittim.valolink.feature.auth.nav

import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import dev.bittim.valolink.feature.auth.ui.screen.otp.OtpScreen
import dev.bittim.valolink.feature.auth.ui.screen.otp.OtpScreenViewModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.koin.core.scope.Scope

interface AuthDestination : UnauthenticatedDestination

val authSerializersModule = SerializersModule {
    polymorphic(NavKey::class) {
        subclass(LandingScreenNav::class)
        subclass(EmailScreenNav::class)
        subclass(OtpScreenNav::class)
    }
}

@Serializable
data object LandingScreenNav : AuthDestination

@Serializable
data object EmailScreenNav : AuthDestination

@Serializable
data object OtpScreenNav : AuthDestination

fun EntryProviderScope<NavKey>.authDestination(
    backStack: NavBackStack<NavKey>,
    scope: Scope
) {
    entry<LandingScreenNav> {
        LandingScreen(navEmail = { backStack.navigateTo(EmailScreenNav) })
    }

    entry<EmailScreenNav> {
        val emailScreenViewModel = remember(scope) { scope.get<EmailScreenViewModel>() }
        val emailScreenState by emailScreenViewModel.state.collectAsStateWithLifecycle()

        EmailScreen(
            state = emailScreenState,
            onEmailChange = { emailScreenViewModel.onEmailChange(it) },
            onNext = { emailScreenViewModel.onNext(navNext = { backStack.navigateTo(OtpScreenNav) })},
            navBack = { backStack.navigateBack() }
        )
    }

    entry<OtpScreenNav> {
        val otpScreenViewModel = remember(scope) { scope.get<OtpScreenViewModel>() }
        val otpScreenState by otpScreenViewModel.state.collectAsStateWithLifecycle()

        OtpScreen(
            state = otpScreenState,
            onOtpChange = { otpScreenViewModel.onOtpChange(it) },
            onComplete = { otpScreenViewModel.onComplete() },
            navBack = { backStack.navigateBack() },
        )
    }
}