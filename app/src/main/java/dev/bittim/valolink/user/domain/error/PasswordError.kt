/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       PasswordError.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   21.12.24, 00:33
 */

package dev.bittim.valolink.user.domain.error

import dev.bittim.valolink.core.domain.util.Error

enum class PasswordError : Error {
    EMPTY,
    TOO_SHORT,
    NO_DIGIT,
    NO_LOWERCASE,
    NO_UPPERCASE,
    NO_SPECIAL_CHAR
}