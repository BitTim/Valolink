/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       Result.kt
 Module:     Valolink.app.main
 Author:     Philipp Lackner
 Source:     https://www.youtube.com/watch?v=MiLN2vs2Oe0
 Modified:   05.04.25, 11:06
 */

package dev.bittim.valolink.core.domain.util

sealed interface Result<out D, out E : Error> {
    data class Ok<out D>(val data: D) : Result<D, Nothing>
    data class Err<out E : Error>(val error: E) : Result<Nothing, E>
}
