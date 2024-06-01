package dev.bittim.valolink.auth.ui.screens.signin

data class SignInState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val authError: String? = null,

    val showForgotDialog: Boolean = false,
    val forgotEmail: String = "",
    val forgotEmailError: String? = null,

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
)
