/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValidateEmailUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   28.05.26, 20:16
 */

package dev.bittim.valolink.feature.auth.domain.usecase

class ValidateEmailUseCase {
    // Same pattern as android.util.Patterns.EMAIL_ADDRESS
    private val emailAddressRegex = Regex(
        "[a-zA-Z0-9+._%\\-]{1,256}" +
                "@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    enum class EmailError {
        EMPTY,
        INVALID
    }

    operator fun invoke(email: String): EmailError? {
        if (email.isEmpty()) return EmailError.EMPTY
        if (!email.matches(emailAddressRegex)) return EmailError.INVALID
        return null
    }
}