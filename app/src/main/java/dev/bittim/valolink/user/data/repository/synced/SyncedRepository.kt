/*
 Copyright (c) 2024-2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       SyncedRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.data.repository.synced

import kotlinx.coroutines.flow.Flow

interface SyncedRepository<T, K> {
    fun get(key: K): Flow<List<T>?>

    suspend fun insert(obj: T): Long
    suspend fun delete(key: K): Long
}