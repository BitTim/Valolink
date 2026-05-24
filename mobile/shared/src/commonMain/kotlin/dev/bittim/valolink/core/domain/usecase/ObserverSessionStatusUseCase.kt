/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ObserverSessionStatusUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 17:55
 */

package dev.bittim.valolink.core.domain.usecase

import dev.bittim.valolink.core.domain.model.AuthState
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserverSessionStatusUseCase(
    private val auth: Auth
) {
    operator fun invoke(): Flow<AuthState> {
        return auth.sessionStatus.map {
            when(it) {
                is SessionStatus.Authenticated -> AuthState.Authenticated
                SessionStatus.Initializing -> AuthState.Loading
                is SessionStatus.NotAuthenticated -> AuthState.Unauthenticated
                is SessionStatus.RefreshFailure -> AuthState.Unauthenticated
            }
        }
    }
}