/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       UsernameError.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   21.12.24, 00:34
 */

package dev.bittim.valolink.user.domain.error

import dev.bittim.valolink.core.domain.util.Error

enum class UsernameError : Error {
    EMPTY,
    TOO_SHORT
}