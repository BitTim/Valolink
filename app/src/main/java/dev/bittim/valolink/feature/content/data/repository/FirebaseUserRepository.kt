package dev.bittim.valolink.feature.content.data.repository

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    private val auth: FirebaseAuth
) : UserRepository {
    override fun hasUser(): Boolean = auth.currentUser != null

    override fun getUsername(): String? {
        if (!hasUser()) return null
        return auth.currentUser?.displayName
    }

    override fun signOut() {
        auth.signOut()
    }
}