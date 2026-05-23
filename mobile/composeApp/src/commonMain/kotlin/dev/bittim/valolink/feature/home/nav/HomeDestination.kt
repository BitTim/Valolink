/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       HomeDestination.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 13:01
 */

package dev.bittim.valolink.feature.home.nav

import androidx.navigation3.runtime.NavKey
import dev.bittim.valolink.core.nav.AppDestination
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

interface HomeDestination : AppDestination {
    override val showBottomNav: Boolean get() = true
}

val homeSerializersModule = SerializersModule {
    polymorphic(NavKey::class) {
        subclass(Home::class)
    }
}

@Serializable
data object Home : HomeDestination