/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       SessionRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 14:44
 */

package dev.bittim.valolink.user.data.repository

import dev.bittim.valolink.user.data.local.entity.UserDataEntity
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.StateFlow

interface SessionRepository {

    // ================================
    //  Session
    // ================================

    fun getSessionStatus(): StateFlow<SessionStatus>
    suspend fun signOut()

    // ================================
    //  User Metadata
    // ================================

    suspend fun getUserInfo(): UserInfo?
    suspend fun getUid(): String?
    suspend fun getUserData(): UserDataEntity?
    suspend fun getUsernameFromMetadata(): String?

    suspend fun getOnboardingStep(): Int?
    suspend fun getUsername(): String?
    suspend fun getPrivate(): Boolean?
    suspend fun getAvatar(): String?

    suspend fun setOnboardingStep(onboardingStep: Int)
    suspend fun setUsername(username: String)
    suspend fun setPrivate(value: Boolean)
    suspend fun setAvatar(avatar: ByteArray)
}
