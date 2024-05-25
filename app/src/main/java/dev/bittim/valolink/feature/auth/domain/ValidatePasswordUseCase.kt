package dev.bittim.valolink.feature.auth.domain

class ValidatePasswordUseCase {
    operator fun invoke(
        password: String,
        confirmPassword: String? = null,
    ): String? {
        if (confirmPassword != null && password != confirmPassword) {
            return "Passwords do not match"
        }

        if (password.isEmpty()) {
            return "Password cannot be empty"
        } else if (password.length < 8) {
            return "Password must be at least 8 characters long"
        }

        return null
    }
}