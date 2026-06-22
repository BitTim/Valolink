/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AuthenticatedDestination.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   04.06.26, 01:18
 */

package dev.bittim.valolink.core.nav

import androidx.navigation3.runtime.NavKey
import dev.bittim.valolink.feature.activity.nav.activitySerializersModule
import dev.bittim.valolink.feature.home.nav.homeSerializersModule
import kotlinx.serialization.modules.plus

interface AuthenticatedDestination : NavKey {
    val showBottomNav: Boolean get() = false
}

val authenticatedSerializersModule = (
    homeSerializersModule +
    activitySerializersModule
)