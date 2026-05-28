/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValidatePasswordUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   28.05.26, 20:52
 */

package dev.bittim.valolink.feature.auth.domain.usecase

class ValidatePasswordUseCase {
    enum class PasswordError {
        EMPTY,
        INVALID
    }

    operator fun invoke(password: String): PasswordError? {
        if (password.isEmpty()) return PasswordError.EMPTY
        return null
    }
}