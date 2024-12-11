package dev.bittim.valolink.user.domain.usecase.validator

import dev.bittim.valolink.core.domain.util.Error
import dev.bittim.valolink.core.domain.util.Result
import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {
    private val _minPasswordLength = 12

    operator fun invoke(
        password: String,
        confirmPassword: String? = null,
    ): Result<Unit, PasswordError> {
        if (confirmPassword != null && password != confirmPassword) {
            return Result.Failure(PasswordError.PASSWORDS_NO_MATCH)
        }

        if (password.isEmpty()) {
            return Result.Failure(PasswordError.EMPTY)
        }

        if (password.length < _minPasswordLength) {
            return Result.Failure(PasswordError.TOO_SHORT)
        }

        if (!password.any { it.isDigit() }) {
            return Result.Failure(PasswordError.NO_DIGIT)
        }

        if (!password.any { it.isUpperCase() }) {
            return Result.Failure(PasswordError.NO_UPPERCASE)
        }

        if (!password.any { !it.isLetterOrDigit() }) {
            return Result.Failure(PasswordError.NO_SPECIAL_CHAR)
        }

        return Result.Success(Unit)
    }
}

enum class PasswordError : Error {
    EMPTY,
    TOO_SHORT,
    NO_DIGIT,
    NO_UPPERCASE,
    NO_SPECIAL_CHAR,
    PASSWORDS_NO_MATCH
}