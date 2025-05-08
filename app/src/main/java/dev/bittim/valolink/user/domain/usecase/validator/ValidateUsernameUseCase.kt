/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ValidateUsernameUseCase.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   05.04.25, 11:06
 */

package dev.bittim.valolink.user.domain.usecase.validator

import dev.bittim.valolink.core.domain.util.Result
import dev.bittim.valolink.user.domain.error.UsernameError
import javax.inject.Inject

class ValidateUsernameUseCase @Inject constructor() {
    operator fun invoke(username: String): Result<Unit, UsernameError> {
        if (username.isEmpty()) {
            return Result.Err(UsernameError.EMPTY)
        }

        if (username.length < 4) {
            return Result.Err(UsernameError.TOO_SHORT)
        }

        return Result.Ok(Unit)
    }
}
