/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityDestination.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   04.06.26, 12:28
 */

package dev.bittim.valolink.feature.activity.nav

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import dev.bittim.valolink.core.nav.AuthenticatedDestination
import dev.bittim.valolink.core.nav.fadeThrough
import dev.bittim.valolink.feature.activity.ui.screen.list.ActivityListScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

interface ActivityDestination : AuthenticatedDestination {
    override val showBottomNav: Boolean get() = true
}

val activitySerializersModule = SerializersModule {
    polymorphic(NavKey::class) {
        subclass(ActivityListScreenNav::class)
    }
}

@Serializable
data object ActivityListScreenNav : ActivityDestination

fun EntryProviderScope<NavKey>.activityDestination(
    backStack: NavBackStack<NavKey>
) {
    entry<ActivityListScreenNav>(metadata = fadeThrough) {
        ActivityListScreen()
    }
}