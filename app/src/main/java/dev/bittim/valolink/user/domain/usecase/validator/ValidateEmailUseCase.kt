package dev.bittim.valolink.user.domain.usecase.validator

import dev.bittim.valolink.core.domain.util.Error
import dev.bittim.valolink.core.domain.util.Result
import dev.bittim.valolink.user.data.validator.EmailPatternValidator
import javax.inject.Inject


class ValidateEmailUseCase @Inject constructor(
    private val validator: EmailPatternValidator,
) {
    operator fun invoke(email: String): Result<Unit, EmailError> {
        if (email.isEmpty()) {
            return Result.Failure(EmailError.EMPTY)
        }

        if (!validator.isValid(email)) {
            return Result.Failure(EmailError.INVALID)
        }

        return Result.Success(Unit)
    }
}

enum class EmailError : Error {
    EMPTY,
    INVALID
}