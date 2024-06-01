package dev.bittim.valolink.auth.domain

data class ValidationUseCases(
    val validateEmail: ValidateEmailUseCase,
    val validateUsername: ValidateUsernameUseCase,
    val validatePassword: ValidatePasswordUseCase,
)
