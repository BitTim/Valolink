/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       Result.kt
 Module:     Valolink.app.main
 Author:     Philipp Lackner
 Source:     https://www.youtube.com/watch?v=MiLN2vs2Oe0
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.core.domain.util

sealed interface Result<out D, out E : Error> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Failure<out E : Error>(val error: E) : Result<Nothing, E>
}