/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       SupabaseAuthRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   21.12.24, 01:12
 */

package dev.bittim.valolink.user.data.repository.auth

import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.util.UiText
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class SupabaseAuthRepository @Inject constructor(
    private val auth: Auth,
) : AuthRepository {
    override suspend fun signIn(
        email: String,
        password: String,
    ): UiText? {
        return try {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            null
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
                null
            }

            if (e.localizedMessage.isNullOrEmpty()) UiText.StringResource(R.string.error_unknown)
            else UiText.DynamicString(e.localizedMessage ?: "")
        }
    }

    override suspend fun createAccount(
        email: String,
        password: String,
    ): UiText? {
        return try {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            null
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
                null
            }

            if (e.localizedMessage.isNullOrEmpty()) UiText.StringResource(R.string.error_unknown)
            else UiText.DynamicString(e.localizedMessage ?: "")
        }
    }
}