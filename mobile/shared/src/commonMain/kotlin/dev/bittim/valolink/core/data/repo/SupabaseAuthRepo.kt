/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SupabaseAuthRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   22.06.26, 16:30
 */

package dev.bittim.valolink.core.data.repo

import dev.bittim.valolink.core.domain.repo.AuthRepo
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.providers.Apple
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.OTP
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.Uuid

class SupabaseAuthRepo(
    private val auth: Auth,
    private val scope: CoroutineScope
) : AuthRepo {
    override val emailCooldown: Duration
        get() = 60.seconds

    override fun signInWithOtp(email: String) {
        scope.launch {
            auth.signUpWith(OTP) { this.email = email }
        }
    }

    override fun verifyOtp(email: String, otp: String) {
        scope.launch {
            auth.verifyEmailOtp(
                type = OtpType.Email.EMAIL,
                email = email,
                token = otp
            )
        }
    }

    override fun signInWithGoogle() {
        scope.launch {
            auth.signInWith(Google)
        }
    }

    override fun signInWithApple() {
        scope.launch {
            auth.signInWith(Apple)
        }
    }

    override fun getCurrentUserId(): Uuid? {
        return auth.currentUserOrNull()?.id?.let { Uuid.parse(it) }
    }

    override fun signOut() {
        scope.launch {
            auth.signOut()
        }
    }
}