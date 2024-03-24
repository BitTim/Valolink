package dev.bittim.valolink.presentation.auth.signin

data class SignInState(
    val isSignInSuccess: Boolean = false,
    val signInError: String? = null,
)
