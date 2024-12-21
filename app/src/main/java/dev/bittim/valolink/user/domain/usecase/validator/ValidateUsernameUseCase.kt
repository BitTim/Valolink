/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       ValidateUsernameUseCase.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   21.12.24, 00:34
 */

package dev.bittim.valolink.user.domain.usecase.validator

import dev.bittim.valolink.core.domain.util.Result
import dev.bittim.valolink.user.domain.error.UsernameError
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