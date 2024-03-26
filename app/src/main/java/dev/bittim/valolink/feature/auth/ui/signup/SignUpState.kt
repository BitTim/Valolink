package dev.bittim.valolink.feature.auth.ui.signup

sealed interface SignUpState {
    data class Input(
        val email: String,
        val username: String,
        val password: String,
        val confirmPassword: String,

        val emailError: String?,
        val usernameError: String?,
        val passwordError: String?,
        val confirmPasswordError: String?,
        val authError: String?
    ) : SignUpState

    data object Loading : SignUpState
    data object Success : SignUpState
}
