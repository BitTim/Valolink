/*
 Copyright (c) 2024-2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserAgentRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.data.repository.synced

import dev.bittim.valolink.user.data.keys.UserAgentKey
import dev.bittim.valolink.user.domain.model.UserAgent
import kotlinx.coroutines.flow.Flow

interface UserAgentRepository : SyncedRepository<UserAgent, UserAgentKey> {
    override fun get(key: UserAgentKey): Flow<List<UserAgent>>

    override suspend fun insert(obj: UserAgent): Long
    override suspend fun delete(key: UserAgentKey): Long
}
