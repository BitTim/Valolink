package dev.bittim.valolink.feature.auth.data

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dev.bittim.valolink.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {
    override fun signIn(email: String, password: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = auth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result))
        }.catch {
            it.printStackTrace()
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun signUp(
        email: String,
        username: String,
        password: String
    ): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            auth.currentUser?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build()
            )
            emit(Resource.Success(result))
        }.catch {
            it.printStackTrace()
            emit(Resource.Error(it.message.toString()))
        }
    }
}