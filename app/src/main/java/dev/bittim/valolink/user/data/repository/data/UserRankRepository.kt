/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserRankRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   20.04.25, 03:29
 */

package dev.bittim.valolink.user.data.repository.data

import dev.bittim.valolink.user.data.remote.dto.UserRankDto
import dev.bittim.valolink.user.domain.model.UserRank
import kotlinx.coroutines.flow.Flow

interface UserRankRepository : UserRepository<UserRank, UserRankDto> {
    fun get(uid: String): Flow<UserRank?>

    suspend fun set(obj: UserRank, toDelete: Boolean): Boolean
    suspend fun set(obj: UserRank): Boolean
    suspend fun delete(obj: UserRank): Boolean

    fun queueWorker(uid: String)
}
