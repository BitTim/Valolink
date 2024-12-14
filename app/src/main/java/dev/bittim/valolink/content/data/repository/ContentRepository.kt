/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       ContentRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.data.repository

import kotlinx.coroutines.flow.Flow

interface ContentRepository<T> {
    suspend fun getByUuid(uuid: String): Flow<T?>
    suspend fun getAll(): Flow<List<T>>

    suspend fun fetch(uuid: String, version: String)
    suspend fun fetchAll(version: String)

    fun queueWorker(uuid: String? = null)
}