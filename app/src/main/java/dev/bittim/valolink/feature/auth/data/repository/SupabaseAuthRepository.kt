package dev.bittim.valolink.feature.auth.data.repository

import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class SupabaseAuthRepository @Inject constructor(
    private val auth: Auth,
) : AuthRepository {
    override suspend fun signIn(
        email: String,
        password: String,
    ): Boolean {
        return try {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun signUp(
        email: String,
        username: String,
        password: String,
    ): Boolean {
        return try {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
                this.data = buildJsonObject {
                    put(
                        "username",
                        username
                    )
                    put(
                        "hasOnboarded",
                        false
                    )
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}