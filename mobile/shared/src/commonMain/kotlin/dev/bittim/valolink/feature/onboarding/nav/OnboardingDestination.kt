/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       OnboardingDestination.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 11:33
 */

package dev.bittim.valolink.feature.onboarding.nav

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import dev.bittim.valolink.core.nav.AppDestination
import dev.bittim.valolink.core.nav.navigateToTopLevel
import dev.bittim.valolink.feature.home.nav.HomeScreenNav
import dev.bittim.valolink.feature.onboarding.ui.screen.landing.LandingScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

interface OnboardingDestination : AppDestination

val onboardingSerializersModule = SerializersModule {
    polymorphic(NavKey::class) {
        subclass(LandingScreenNav::class)
    }
}

@Serializable
data object LandingScreenNav : OnboardingDestination

fun EntryProviderScope<NavKey>.onboardingDestination(
    backStack: NavBackStack<NavKey>
) {
    entry<LandingScreenNav> {
        LandingScreen(navHome = { backStack.navigateToTopLevel(HomeScreenNav) })
    }
}