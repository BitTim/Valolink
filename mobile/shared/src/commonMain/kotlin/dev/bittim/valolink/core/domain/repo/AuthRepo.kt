/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AuthRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   29.05.26, 16:11
 */

package dev.bittim.valolink.core.domain.repo

interface AuthRepo {
    fun signInWithOtp(email: String)
    fun verifyOtp(email: String, otp: String)
    fun signInWithGoogle()
    fun signInWithApple()

    fun signOut()
}