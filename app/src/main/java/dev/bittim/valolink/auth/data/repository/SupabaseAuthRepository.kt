package dev.bittim.valolink.auth.data.repository

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

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
            if (e is CancellationException) throw e
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
                        "display_name",
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
            if (e is CancellationException) throw e
            e.printStackTrace()
            false
        }
    }
}