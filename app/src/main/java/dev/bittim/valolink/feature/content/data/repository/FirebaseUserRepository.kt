package dev.bittim.valolink.feature.content.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    private val auth: FirebaseAuth
) : UserRepository {
    override fun hasUser(): Flow<Boolean> = flow {
        while (true) {
            emit(auth.currentUser != null)
            delay(10000)
        }
    }

    override fun getUsername(): String? {
        return auth.currentUser?.displayName
    }

    override fun signOut() {
        auth.signOut()
    }
}