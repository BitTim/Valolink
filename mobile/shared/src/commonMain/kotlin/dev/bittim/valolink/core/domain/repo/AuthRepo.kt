/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AuthRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   22.06.26, 16:30
 */

package dev.bittim.valolink.core.domain.repo

import kotlin.time.Duration
import kotlin.uuid.Uuid

interface AuthRepo {
    val emailCooldown: Duration

    fun signInWithOtp(email: String)
    fun verifyOtp(email: String, otp: String)
    fun signInWithGoogle()
    fun signInWithApple()

    fun getCurrentUserId(): Uuid?

    fun signOut()
}