package dev.bittim.valolink.feature.auth.domain

class ValidateUsernameUseCase {
    operator fun invoke(username: String): String? {
        if (username.isEmpty()) return "Username cannot be empty"
        if (username.length < 4) return "Username must be at least 4 characters long"
        return null
    }
}