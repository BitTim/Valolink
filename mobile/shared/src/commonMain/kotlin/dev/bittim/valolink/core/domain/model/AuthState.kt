/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AuthState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 17:24
 */

package dev.bittim.valolink.core.domain.model

sealed interface AuthState {
    data object Loading : AuthState
    data object Authenticated : AuthState
    data object Unauthenticated : AuthState
}