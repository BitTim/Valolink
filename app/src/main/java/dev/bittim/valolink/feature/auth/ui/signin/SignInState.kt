package dev.bittim.valolink.feature.auth.ui.signin

import dev.bittim.valolink.core.ui.UiText

sealed interface SignInState {
    data class Input(
        val email: String,
        val password: String,
        val emailError: UiText?,
        val passwordError: UiText?,
        val authError: UiText?,

        val showForgotDialog: Boolean,
        val forgotEmail: String,
        val forgotEmailError: UiText?
    ) : SignInState
    data object Loading : SignInState
    data object Success : SignInState
}
