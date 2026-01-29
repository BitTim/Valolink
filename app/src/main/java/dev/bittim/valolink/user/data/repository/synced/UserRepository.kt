/*
 Copyright (c) 2024-2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.data.repository.synced

import dev.bittim.valolink.user.data.keys.UserKey
import dev.bittim.valolink.user.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository : SyncedRepository<User, UserKey> {
    override fun get(key: UserKey): Flow<List<User>?>

    override suspend fun insert(obj: User): Long
    override suspend fun delete(key: UserKey): Long
}
