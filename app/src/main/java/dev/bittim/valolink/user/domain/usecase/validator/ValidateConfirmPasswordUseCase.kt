package dev.bittim.valolink.user.domain.usecase.validator

import dev.bittim.valolink.core.domain.util.Error
import dev.bittim.valolink.core.domain.util.Result
import javax.inject.Inject

class ValidateConfirmPasswordUseCase @Inject constructor() {
    operator fun invoke(
        password: String,
        confirmPassword: String,
    ): Result<Unit, ConfirmPasswordError> {
        if (confirmPassword.isEmpty()) {
            return Result.Failure(ConfirmPasswordError.EMPTY)
        }

        if (password != confirmPassword) {
            return Result.Failure(ConfirmPasswordError.NO_MATCH)
        }

        return Result.Success(Unit)
    }
}

enum class ConfirmPasswordError : Error {
    EMPTY,
    NO_MATCH
}