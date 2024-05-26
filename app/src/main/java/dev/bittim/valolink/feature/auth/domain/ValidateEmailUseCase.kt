package dev.bittim.valolink.feature.auth.domain

import dev.bittim.valolink.feature.auth.data.validator.EmailPatternValidator


class ValidateEmailUseCase(
    private val validator: EmailPatternValidator,
) {
    operator fun invoke(email: String): String? {
        if (email.isEmpty()) {
            return "Email cannot be empty"
        } else if (!validator.isValid(email)) {
            return "Invalid email format"
        }

        return null
    }
}