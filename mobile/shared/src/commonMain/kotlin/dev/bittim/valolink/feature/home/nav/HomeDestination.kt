/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       HomeDestination.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 17:49
 */

package dev.bittim.valolink.feature.home.nav

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import dev.bittim.valolink.core.nav.AuthenticatedDestination
import dev.bittim.valolink.feature.home.ui.screen.home.HomeScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

interface HomeDestination : AuthenticatedDestination {
    override val showBottomNav: Boolean get() = true
}

val homeSerializersModule = SerializersModule {
    polymorphic(NavKey::class) {
        subclass(HomeScreenNav::class)
    }
}

@Serializable
data object HomeScreenNav : HomeDestination

fun EntryProviderScope<NavKey>.homeDestination(
    backStack: NavBackStack<NavKey>
) {
    entry<HomeScreenNav> {
        HomeScreen(navLanding = {  })
    }
}