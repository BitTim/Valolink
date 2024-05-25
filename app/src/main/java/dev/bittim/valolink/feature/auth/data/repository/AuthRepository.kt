package dev.bittim.valolink.feature.auth.data.repository

import io.appwrite.models.User

interface AuthRepository {
    suspend fun getUser(): User<Map<String, Any>>?

    suspend fun signIn(
        email: String,
        password: String,
    ): User<Map<String, Any>>?

    suspend fun signUp(
        email: String,
        username: String,
        password: String,
    ): User<Map<String, Any>>?

    fun forgotPassword(email: String): Boolean
}