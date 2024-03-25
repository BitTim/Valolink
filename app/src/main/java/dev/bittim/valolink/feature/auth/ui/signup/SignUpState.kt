package dev.bittim.valolink.feature.auth.ui.signup

data class SignUpState(
    val isLoading: Boolean = false,
    val success: String? = null,
    val error: String? = null
)
