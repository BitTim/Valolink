/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserLevelRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   16.04.25, 19:18
 */

package dev.bittim.valolink.user.data.repository.data

import dev.bittim.valolink.user.data.remote.dto.UserLevelDto
import dev.bittim.valolink.user.domain.model.UserLevel
import kotlinx.coroutines.flow.Flow

interface UserLevelRepository : UserRepository<UserLevel, UserLevelDto> {
    fun getAll(uid: String): Flow<List<UserLevel>>
    fun get(uid: String, uuid: String): Flow<UserLevel?>

    suspend fun set(obj: UserLevel, toDelete: Boolean): Boolean
    suspend fun set(obj: UserLevel): Boolean
    suspend fun delete(obj: UserLevel): Boolean

    fun queueWorker(userContract: String)
}
