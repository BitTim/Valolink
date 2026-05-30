/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SubmitOtpUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.05.26, 00:56
 */

package dev.bittim.valolink.core.domain.usecase

import dev.bittim.valolink.core.domain.repo.AuthRepo

class SubmitOtpUseCase(
    private val authRepo: AuthRepo,
) {
    operator fun invoke(email: String, otp: String) {
        authRepo.verifyOtp(email, otp)
    }
}