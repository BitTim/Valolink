/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValidateOtpUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.05.26, 01:06
 */

package dev.bittim.valolink.feature.auth.domain.usecase

class ValidateOtpUseCase {
    enum class OtpError {
        EMPTY,
        LENGTH,
        INVALID
    }

    operator fun invoke(otp: String): OtpError? {
        return if (otp.isEmpty()) OtpError.EMPTY
        else if (otp.length != 8) OtpError.LENGTH
        else if (!otp.all { it.isDigit() }) OtpError.INVALID
        else null
    }
}