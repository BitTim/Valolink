package dev.bittim.valolink.feature.auth.ui.screens.signup

import dev.bittim.valolink.core.ui.UiText

sealed interface SignUpState {
    data class Input(
        val email: String,
        val username: String,
        val password: String,
        val confirmPassword: String,

        val emailError: UiText?,
        val usernameError: UiText?,
        val passwordError: UiText?,
        val confirmPasswordError: UiText?,
        val authError: UiText?
    ) : SignUpState

    data object Loading : SignUpState
    data object Success : SignUpState
}
