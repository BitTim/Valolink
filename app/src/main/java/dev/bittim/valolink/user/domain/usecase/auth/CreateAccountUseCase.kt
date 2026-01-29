/*
 Copyright (c) 2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       CreateAccountUseCase.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.domain.usecase.auth

import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.util.UiText
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class CreateAccountUseCase @Inject constructor(
    private val auth: Auth
) {
    suspend operator fun invoke(
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
            }

            if (e.localizedMessage.isNullOrEmpty()) UiText.StringResource(R.string.error_unknown)
            else UiText.DynamicString(e.localizedMessage ?: "")
        }
    }
}