/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoRankTableDao.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.06.26, 03:01
 */

package dev.bittim.valolink.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.core.data.local.entity.ValoRankTableEntity
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface ValoRankTableDao {
    @Upsert
    suspend fun upsert(rankTables: List<ValoRankTableEntity>)

    @Query("SELECT * FROM valo_rank_tables WHERE uuid = :uuid")
    fun get(uuid: Uuid): Flow<ValoRankTableEntity?>

    @Query("SELECT * FROM valo_rank_tables")
    fun get(): Flow<List<ValoRankTableEntity>>
}