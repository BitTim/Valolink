/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       SessionRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   16.04.25, 19:18
 */

package dev.bittim.valolink.user.data.repository

import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    companion object {
        const val LOCAL_AVATAR_FILENAME = "avatar.jpg"
    }

    // ================================
    //  Session
    // ================================

    fun isAuthenticated(): Flow<Boolean?>
    suspend fun signOut()

    fun isLocal(): Flow<Boolean?>
    suspend fun setLocal(local: Boolean)

    // ================================
    //  User Metadata
    // ================================

    fun getUserInfo(): Flow<UserInfo?>
    fun getUid(): Flow<String?>
    fun getUsernameFromMetadata(): Flow<String?>
}
