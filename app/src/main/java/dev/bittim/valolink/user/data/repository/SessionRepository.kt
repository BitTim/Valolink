/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       SessionRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.user.data.repository

import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserUpdateBuilder
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
    suspend fun getHasOnboarded(): Boolean?
    suspend fun getUsernameFromMetadata(): String?
    suspend fun updateUserInfo(userInfo: UserUpdateBuilder.() -> Unit)
}
