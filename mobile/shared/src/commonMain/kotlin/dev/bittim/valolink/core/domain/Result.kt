/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       Result.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   12.06.26, 00:19
 */

package dev.bittim.valolink.core.domain

sealed interface Result<out D, out E> {
    data class Ok<out D, out E>(val data: D) : Result<D, E>
    data class Err<out D, out E>(val error: E) : Result<D, E>
}