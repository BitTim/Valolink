/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SignInWithOtpUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   29.05.26, 16:11
 */

package dev.bittim.valolink.core.domain.usecase

import dev.bittim.valolink.core.domain.repo.AuthRepo

class SignInWithOtpUseCase(
    private val authRepo: AuthRepo
) {
    operator fun invoke(email: String) {
        authRepo.signInWithOtp(email)
    }
}