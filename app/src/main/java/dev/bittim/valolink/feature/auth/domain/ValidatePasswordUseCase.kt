package dev.bittim.valolink.feature.auth.domain

import dev.bittim.valolink.core.domain.Error
import dev.bittim.valolink.core.domain.Result

class ValidatePasswordUseCase {
    operator fun invoke(
        password: String,
        confirmPassword: String? = null
    ): Result<Unit, PasswordError> {
        if (confirmPassword != null && password != confirmPassword) {
            return Result.Error(PasswordError.MISMATCH)
        }

        if (password.isEmpty()) {
            return Result.Error(PasswordError.EMPTY)
        } else if (password.length < 8) {
            return Result.Error(PasswordError.TOO_SHORT)
        }

        return Result.Success(Unit)
    }

    enum class PasswordError : Error {
        EMPTY,
        MISMATCH,
        TOO_SHORT
    }
}