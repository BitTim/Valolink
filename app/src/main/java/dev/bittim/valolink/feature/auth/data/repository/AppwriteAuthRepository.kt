package dev.bittim.valolink.feature.auth.data.repository

import io.appwrite.ID
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.User
import io.appwrite.services.Account
import javax.inject.Inject

class AppwriteAuthRepository @Inject constructor(
    private val account: Account,
) : AuthRepository {
    override suspend fun getUser(): User<Map<String, Any>>? {
        return try {
            account.get()
        } catch (e: AppwriteException) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun signIn(
        email: String,
        password: String,
    ): User<Map<String, Any>>? {
        return try {
            account.createEmailPasswordSession(
                email,
                password
            )
            getUser()
        } catch (e: AppwriteException) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun signUp(
        email: String,
        username: String,
        password: String,
    ): User<Map<String, Any>>? {
        return try {
            account.create(
                ID.unique(),
                email,
                password
            )
            signIn(
                email,
                password
            )
            account.updateName(username)
        } catch (e: AppwriteException) {
            e.printStackTrace()
            null
        }
    }

    // TODO: Not yet implemented
    override fun forgotPassword(email: String): Boolean {
        return true
    }
}