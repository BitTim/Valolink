package dev.bittim.valolink.feature.auth.data.repository

import com.google.firebase.auth.AuthResult
import dev.bittim.valolink.core.domain.Error
import dev.bittim.valolink.core.domain.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signIn(email: String, password: String): Flow<Result<AuthResult, AuthError>>
    fun signUp(
        email: String,
        username: String,
        password: String
    ): Flow<Result<AuthResult, AuthError>>

    fun forgotPassword(email: String): Flow<Result<Unit, AuthError>>

    enum class AuthError : Error {
        GENERIC
    }
}