/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       ValidatePasswordUseCase.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.user.domain.usecase.validator

import dev.bittim.valolink.core.domain.util.Error
import dev.bittim.valolink.core.domain.util.Result
import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {
    operator fun invoke(
        password: String,
    ): Result<Unit, PasswordError> {
        if (password.isEmpty()) {
            return Result.Failure(PasswordError.EMPTY)
        }

        if (password.length < MIN_PASSWORD_LENGTH) {
            return Result.Failure(PasswordError.TOO_SHORT)
        }

        if (!password.any { it.isDigit() }) {
            return Result.Failure(PasswordError.NO_DIGIT)
        }

        if (!password.any { it.isLowerCase() }) {
            return Result.Failure(PasswordError.NO_LOWERCASE)
        }

        if (!password.any { it.isUpperCase() }) {
            return Result.Failure(PasswordError.NO_UPPERCASE)
        }

        if (!password.any { !it.isLetterOrDigit() }) {
            return Result.Failure(PasswordError.NO_SPECIAL_CHAR)
        }

        return Result.Success(Unit)
    }

    companion object {
        const val MIN_PASSWORD_LENGTH = 12
    }
}

enum class PasswordError : Error {
    EMPTY,
    TOO_SHORT,
    NO_DIGIT,
    NO_LOWERCASE,
    NO_UPPERCASE,
    NO_SPECIAL_CHAR
}