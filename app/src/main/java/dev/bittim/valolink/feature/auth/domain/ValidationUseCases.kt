package dev.bittim.valolink.feature.auth.domain

data class ValidationUseCases(
    val validateEmail: ValidateEmailUseCase,
    val validateUsername: ValidateUsernameUseCase,
    val validatePassword: ValidatePasswordUseCase
)
