/*
 Copyright (c) 2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       SignOutUseCase.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.domain.usecase.auth

import io.github.jan.supabase.auth.Auth
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val auth: Auth
) {
    suspend operator fun invoke() {
        auth.signOut()
    }
}