/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ValidatePasswordUseCase.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   05.04.25, 11:06
 */

package dev.bittim.valolink.user.domain.usecase.validator

import dev.bittim.valolink.core.domain.util.Result
import dev.bittim.valolink.user.domain.error.PasswordError
import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {
    operator fun invoke(
        password: String,
    ): Result<Unit, PasswordError> {
        if (password.isEmpty()) {
            return Result.Err(PasswordError.EMPTY)
        }

        if (password.length < MIN_PASSWORD_LENGTH) {
            return Result.Err(PasswordError.TOO_SHORT)
        }

        if (!password.any { it.isDigit() }) {
            return Result.Err(PasswordError.NO_DIGIT)
        }

        if (!password.any { it.isLowerCase() }) {
            return Result.Err(PasswordError.NO_LOWERCASE)
        }

        if (!password.any { it.isUpperCase() }) {
            return Result.Err(PasswordError.NO_UPPERCASE)
        }

        if (!password.any { !it.isLetterOrDigit() }) {
            return Result.Err(PasswordError.NO_SPECIAL_CHAR)
        }

        return Result.Ok(Unit)
    }

    companion object {
        const val MIN_PASSWORD_LENGTH = 12
    }
}
