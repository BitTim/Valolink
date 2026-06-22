/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       RootScreenState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 17:24
 */

package dev.bittim.valolink.core.ui.screen.root

import dev.bittim.valolink.core.domain.model.AuthState

data class RootScreenState(
    val authState: AuthState = AuthState.Loading
)
