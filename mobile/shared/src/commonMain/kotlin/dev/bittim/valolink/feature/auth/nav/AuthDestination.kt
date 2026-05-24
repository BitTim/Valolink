/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AuthDestination.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 17:49
 */

package dev.bittim.valolink.feature.auth.nav

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import dev.bittim.valolink.core.nav.UnauthenticatedDestination
import dev.bittim.valolink.feature.auth.ui.screen.landing.LandingScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

interface AuthDestination : UnauthenticatedDestination

val authSerializersModule = SerializersModule {
    polymorphic(NavKey::class) {
        subclass(LandingScreenNav::class)
    }
}

@Serializable
data object LandingScreenNav : AuthDestination

fun EntryProviderScope<NavKey>.authDestination(
    backStack: NavBackStack<NavKey>
) {
    entry<LandingScreenNav> {
        LandingScreen(navHome = {  })
    }
}