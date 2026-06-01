/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AuthRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   01.06.26, 16:31
 */

package dev.bittim.valolink.core.domain.repo

import kotlin.time.Duration

interface AuthRepo {
    val emailCooldown: Duration

    fun signInWithOtp(email: String)
    fun verifyOtp(email: String, otp: String)
    fun signInWithGoogle()
    fun signInWithApple()

    fun signOut()
}