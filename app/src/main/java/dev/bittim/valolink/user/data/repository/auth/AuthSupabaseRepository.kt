/*
 Copyright (c) 2024-2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       AuthSupabaseRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.data.repository.auth

import androidx.lifecycle.DefaultLifecycleObserver
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthSupabaseRepository @Inject constructor(
    private val auth: Auth,
) : AuthRepository, DefaultLifecycleObserver {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUserInfo(): Flow<UserInfo?> {
        return auth.sessionStatus.map { status -> if (status is SessionStatus.Authenticated) status.session.user else null }
    }
}
