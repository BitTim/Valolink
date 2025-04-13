/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       AuthRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 14:44
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

    suspend fun sendPasswordResetEmail(
        email: String,
    ): UiText?

    suspend fun resetPassword(
        password: String
    ): UiText?
}
