package dev.bittim.valolink.feature.auth.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dev.bittim.valolink.core.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {
    override fun signIn(
        email: String,
        password: String
    ): Flow<Result<AuthResult, AuthRepository.AuthError>> {
        return flow<Result<AuthResult, AuthRepository.AuthError>> {
            emit(Result.Loading())
            val result = auth.signInWithEmailAndPassword(email, password).await()
            emit(Result.Success(result))
        }.catch {
            it.printStackTrace()
            emit(Result.Error(AuthRepository.AuthError.GENERIC)) // "it" contains error
        }
    }

    override fun signUp(
        email: String,
        username: String,
        password: String
    ): Flow<Result<AuthResult, AuthRepository.AuthError>> {
        return flow<Result<AuthResult, AuthRepository.AuthError>> {
            emit(Result.Loading())
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            auth.currentUser?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build()
            )
            emit(Result.Success(result))
        }.catch {
            it.printStackTrace()
            emit(Result.Error(AuthRepository.AuthError.GENERIC)) // "it" contains error
        }
    }

    override fun forgotPassword(email: String): Flow<Result<Unit, AuthRepository.AuthError>> {
        return flow<Result<Unit, AuthRepository.AuthError>> {
            emit(Result.Loading())
            auth.sendPasswordResetEmail(email).await()
            emit(Result.Success(Unit))
        }.catch {
            it.printStackTrace()
            emit(Result.Error(AuthRepository.AuthError.GENERIC)) // "it" contains error
        }
    }
}