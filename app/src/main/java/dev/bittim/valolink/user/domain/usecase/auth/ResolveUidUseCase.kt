/*
 Copyright (c) 2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ResolveUidUseCase.kt
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
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ResolveUidUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    @OptIn(ExperimentalUuidApi::class)
    fun watch(): Flow<Uuid?> {
        return authRepository.getUserInfo().map { userInfo -> userInfo?.id?.let { Uuid.parse(it) } }
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun current(): Uuid? {
        return watch().firstOrNull()
    }
}