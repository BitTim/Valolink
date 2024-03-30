package dev.bittim.valolink.feature.auth.domain

import dev.bittim.valolink.core.domain.Error
import dev.bittim.valolink.core.domain.Result

class ValidateUsernameUseCase {
    operator fun invoke(username: String): Result<Unit, UsernameError> {
        if (username.isEmpty()) return Result.Error(UsernameError.EMPTY)
        if (username.length < 4) return Result.Error(UsernameError.TOO_SHORT)
        return Result.Success(Unit)
    }

    enum class UsernameError : Error {
        EMPTY,
        TOO_SHORT
    }
}