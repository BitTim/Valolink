package dev.bittim.valolink.presentation.auth.signin

data class SignInResult(
    val data: UserData?,
    val error: String?
)

data class UserData(
    val userId: String,
    val name: String?,
    val profilePictureUrl: String?
)