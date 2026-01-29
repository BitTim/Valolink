/*
 Copyright (c) 2024-2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserLevelRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.data.repository.synced

import dev.bittim.valolink.user.data.keys.UserLevelKey
import dev.bittim.valolink.user.domain.model.UserLevel
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi

interface UserLevelRepository : SyncedRepository<UserLevel, UserLevelKey> {
    @OptIn(ExperimentalUuidApi::class)
    override fun get(key: UserLevelKey): Flow<List<UserLevel>>

    override suspend fun insert(obj: UserLevel): Long
    override suspend fun delete(key: UserLevelKey): Long
}
