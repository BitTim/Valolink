package dev.bittim.valolink.auth.ui.screens.signup

data class SignUpState(
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",

    val emailError: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val authError: String? = null,

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
)
