/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       Outcome.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.09.26, 15:23
 */

package dev.bittim.valolink.core.domain

sealed interface Outcome<out D, out E> {
    data class Ok<out D, out E>(val data: D) : Outcome<D, E>
    data class Err<out D, out E>(val error: E) : Outcome<D, E>
}