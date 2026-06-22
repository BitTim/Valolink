/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       UnauthenticatedDestination.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 17:41
 */

package dev.bittim.valolink.core.nav

import androidx.navigation3.runtime.NavKey
import dev.bittim.valolink.feature.auth.nav.authSerializersModule

interface UnauthenticatedDestination : NavKey

val unauthenticatedSerializersModule = (
    authSerializersModule
)