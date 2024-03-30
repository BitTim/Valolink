package dev.bittim.valolink.feature.auth.domain

import dev.bittim.valolink.core.domain.Error
import dev.bittim.valolink.core.domain.Result
import dev.bittim.valolink.feature.auth.data.validator.EmailPatternValidator


class ValidateEmailUseCase(
    private val validator: EmailPatternValidator
) {
    operator fun invoke(email: String): Result<Unit, EmailError> {
        if (email.isEmpty()) {
            return Result.Error(EmailError.EMPTY)
        } else if (!validator.isValid(email)) {
            return Result.Error(EmailError.INVALID)
        }

        return Result.Success(Unit)
    }

    enum class EmailError : Error {
        EMPTY,
        INVALID
    }
}