package dev.bittim.valolink.feature.auth.data

import com.google.firebase.auth.AuthResult
import dev.bittim.valolink.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signIn(email: String, password: String): Flow<Resource<AuthResult>>
    fun signUp(email: String, password: String): Flow<Resource<AuthResult>>
}