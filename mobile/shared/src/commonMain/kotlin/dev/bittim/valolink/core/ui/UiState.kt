/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       UiState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.09.26, 15:23
 */

package dev.bittim.valolink.core.ui

import dev.bittim.valolink.core.domain.Outcome

sealed interface UiState<out D, out E> {
    data object Loading : UiState<Nothing, Nothing>
    data class Ready<D, E>(val outcome: Outcome<D, E>) : UiState<D, E>
}