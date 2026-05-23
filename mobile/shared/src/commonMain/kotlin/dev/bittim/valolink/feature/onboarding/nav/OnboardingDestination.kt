/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       OnboardingDestination.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.feature.onboarding.nav

import androidx.navigation3.runtime.NavKey
import dev.bittim.valolink.core.nav.AppDestination
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

interface OnboardingDestination : AppDestination

val onboardingSerializersModule = SerializersModule {
    polymorphic(NavKey::class) {
        subclass(Landing::class)
    }
}

@Serializable
data object Landing : OnboardingDestination