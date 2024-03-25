package dev.bittim.valolink.feature.auth.ui.signin

sealed interface SignInState {
    data object Loading : SignInState
    data class Success(val success: String) : SignInState
    data class Error(val error: String) : SignInState
}
