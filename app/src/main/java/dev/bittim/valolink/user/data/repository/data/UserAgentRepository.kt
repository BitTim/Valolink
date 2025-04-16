/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserAgentRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   16.04.25, 19:18
 */

package dev.bittim.valolink.user.data.repository.data

import dev.bittim.valolink.user.data.remote.dto.UserAgentDto
import dev.bittim.valolink.user.domain.model.UserAgent
import kotlinx.coroutines.flow.Flow

interface UserAgentRepository : UserRepository<UserAgent, UserAgentDto> {
    fun getAllWithCurrentUser(): Flow<List<UserAgent>>
    fun getAll(uid: String): Flow<List<UserAgent>>
    fun getWithCurrentUser(uuid: String): Flow<UserAgent?>
    fun get(uid: String, uuid: String): Flow<UserAgent?>

    suspend fun set(obj: UserAgent, toDelete: Boolean): Boolean
    suspend fun set(obj: UserAgent): Boolean
    suspend fun delete(obj: UserAgent): Boolean

    fun queueWorker(uid: String)
}
