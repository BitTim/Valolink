/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoRankDao.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.06.26, 03:08
 */

package dev.bittim.valolink.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.core.data.local.entity.ValoRankEntity
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface ValoRankDao {
    @Upsert
    suspend fun upsert(ranks: List<ValoRankEntity>)

    @Query("SELECT * FROM valo_ranks WHERE rankTable = :rankTable AND tier = :tier LIMIT 1")
    fun get(rankTable: Uuid, tier: Int): Flow<ValoRankEntity?>

    @Query("SELECT * FROM valo_ranks WHERE rankTable = :rankTable")
    fun get(rankTable: Uuid): Flow<List<ValoRankEntity>>

    @Query("SELECT * FROM valo_ranks")
    fun get(): Flow<List<ValoRankEntity>>
}