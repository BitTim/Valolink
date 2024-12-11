package dev.bittim.valolink.user.domain.usecase.validator

import dev.bittim.valolink.core.domain.util.Error
import dev.bittim.valolink.core.domain.util.Result
import javax.inject.Inject

class ValidateUsernameUseCase @Inject constructor() {
    operator fun invoke(username: String): Result<Unit, UsernameError> {
        if (username.isEmpty()) {
            return Result.Failure(UsernameError.EMPTY)
        }

        if (username.length < 4) {
            return Result.Failure(UsernameError.TOO_SHORT)
        }

        return Result.Success(Unit)
    }
}

enum class UsernameError : Error {
    EMPTY,
    TOO_SHORT
}