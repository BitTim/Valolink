/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       ValidateEmailUseCase.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   21.12.24, 00:33
 */

package dev.bittim.valolink.user.domain.usecase.validator

import dev.bittim.valolink.core.domain.util.Result
import dev.bittim.valolink.user.data.validator.EmailPatternValidator
import dev.bittim.valolink.user.domain.error.EmailError
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