package dev.bittim.valolink.feature.auth.ui.signin

sealed interface SignInState {
    data class Input(
        val email: String,
        val password: String,
        val emailError: String?,
        val passwordError: String?,
        val authError: String?
    ) : SignInState
    data object Loading : SignInState
    data object Success : SignInState
}
