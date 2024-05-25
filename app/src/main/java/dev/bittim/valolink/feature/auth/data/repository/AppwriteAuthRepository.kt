package dev.bittim.valolink.feature.auth.data.repository

import dev.bittim.valolink.core.domain.Result
import io.appwrite.ID
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.User
import io.appwrite.services.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AppwriteAuthRepository @Inject constructor(
    private val account: Account,
) : AuthRepository {
    override suspend fun getUser(): User<Map<String, Any>>? {
        return try {
            account.get()
        } catch (e: AppwriteException) {
            null
        }
    }

    override fun signIn(
        email: String,
        password: String,
    ): Flow<Result<User<Map<String, Any>>?, AuthRepository.AuthError>> {
        return flow<Result<User<Map<String, Any>>?, AuthRepository.AuthError>> {
            emit(Result.Loading())
            account.createEmailPasswordSession(
                email,
                password
            )
            val result = getUser()
            emit(Result.Success(result))
        }.catch {
            it.printStackTrace()
            emit(Result.Error(AuthRepository.AuthError.GENERIC)) // "it" contains error
        }
    }

    override fun signUp(
        email: String,
        username: String,
        password: String,
    ): Flow<Result<User<Map<String, Any>>?, AuthRepository.AuthError>> {
        return flow<Result<User<Map<String, Any>>?, AuthRepository.AuthError>> {
            emit(Result.Loading())
            account.create(
                ID.unique(),
                email,
                password
            )
            signIn(
                email,
                password
            )
            val result = account.updateName(username)
            emit(Result.Success(result))
        }.catch {
            it.printStackTrace()
            emit(Result.Error(AuthRepository.AuthError.GENERIC)) // "it" contains error
        }
    }

    override fun forgotPassword(email: String): Flow<Result<Unit, AuthRepository.AuthError>> {
        TODO("Not yet implemented")
    }

    override suspend fun checkSessionExists(): Boolean {
        return try {
            account.getSession("current")
            true
        } catch (e: AppwriteException) {
            e.printStackTrace()
            false
        }
    }
}