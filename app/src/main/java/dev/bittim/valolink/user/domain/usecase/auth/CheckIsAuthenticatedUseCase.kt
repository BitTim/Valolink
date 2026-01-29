/*
 Copyright (c) 2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       CheckIsAuthenticatedUseCase.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.domain.usecase.auth

import dev.bittim.valolink.user.data.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CheckIsAuthenticatedUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun watch(): Flow<Boolean> {
        return authRepository.getUserInfo().map { it != null }
    }

    suspend fun current(): Boolean {
        return watch().firstOrNull() ?: false
    }
}