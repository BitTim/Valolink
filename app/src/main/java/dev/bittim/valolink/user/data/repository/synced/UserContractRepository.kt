/*
 Copyright (c) 2024-2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserContractRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.data.repository.synced

import dev.bittim.valolink.user.data.keys.UserContractKey
import dev.bittim.valolink.user.domain.model.UserContract
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi

interface UserContractRepository : SyncedRepository<UserContract, UserContractKey> {
    @OptIn(ExperimentalUuidApi::class)
    override fun get(key: UserContractKey): Flow<List<UserContract>>

    override suspend fun insert(obj: UserContract): Long
    override suspend fun delete(key: UserContractKey): Long
}
