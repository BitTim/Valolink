/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AppDestination.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.nav

import androidx.navigation3.runtime.NavKey
import dev.bittim.valolink.feature.home.nav.homeSerializersModule
import dev.bittim.valolink.feature.onboarding.nav.onboardingSerializersModule
import kotlinx.serialization.modules.plus

interface AppDestination : NavKey {
    val showBottomNav: Boolean get() = false
}

val appSerializersModule = (
    onboardingSerializersModule
    + homeSerializersModule
)