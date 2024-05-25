package dev.bittim.valolink.feature.auth.data.repository

import dev.bittim.valolink.core.domain.Error
import dev.bittim.valolink.core.domain.Result
import io.appwrite.models.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun getUser(): User<Map<String, Any>>?

    fun signIn(
        email: String,
        password: String,
    ): Flow<Result<User<Map<String, Any>>?, AuthError>>

    fun signUp(
        email: String,
        username: String,
        password: String,
    ): Flow<Result<User<Map<String, Any>>?, AuthError>>

    fun forgotPassword(email: String): Flow<Result<Unit, AuthError>>

    enum class AuthError : Error {
        GENERIC
    }

    suspend fun checkSessionExists(): Boolean
}