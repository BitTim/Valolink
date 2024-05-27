package dev.bittim.valolink.feature.auth.data.repository

interface AuthRepository {
    suspend fun signIn(
        email: String,
        password: String,
    ): Boolean

    suspend fun signUp(
        email: String,
        username: String,
        password: String,
    ): Boolean
}