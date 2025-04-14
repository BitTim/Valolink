/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       SessionRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.04.25, 02:40
 */

package dev.bittim.valolink.user.data.repository

import dev.bittim.valolink.user.data.local.entity.UserDataEntity
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.Flow

interface SessionRepository {

    // ================================
    //  Session
    // ================================

    fun getAuthenticated(): Flow<Boolean>
    suspend fun signOut()

    suspend fun isLocal(): Flow<Boolean>
    suspend fun createLocalUser()
    suspend fun createUser()

    // ================================
    //  User Metadata
    // ================================

    suspend fun getUserInfo(): UserInfo?
    suspend fun getUid(): String?
    suspend fun getUserData(): Flow<UserDataEntity?>
    suspend fun getUsernameFromMetadata(): String?

    suspend fun getOnboardingStep(): Flow<Int?>
    suspend fun getUsername(): Flow<String?>
    suspend fun getPrivate(): Flow<Boolean?>
    suspend fun getAvatar(): Flow<String?>

    suspend fun setOnboardingStep(onboardingStep: Int)
    suspend fun setUsername(username: String)
    suspend fun setPrivate(value: Boolean)
    suspend fun setAvatar(avatar: ByteArray)
}
