/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       AuthRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   21.12.24, 01:24
 */

package dev.bittim.valolink.user.data.repository.auth

import dev.bittim.valolink.core.ui.util.UiText

interface AuthRepository {
    suspend fun signIn(
        email: String,
        password: String,
    ): UiText?

    suspend fun createAccount(
        email: String,
        password: String,
    ): UiText?
}