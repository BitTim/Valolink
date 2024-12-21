/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       EmailError.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   21.12.24, 00:33
 */

package dev.bittim.valolink.user.domain.error

import dev.bittim.valolink.core.domain.util.Error

enum class EmailError : Error {
    EMPTY,
    INVALID
}