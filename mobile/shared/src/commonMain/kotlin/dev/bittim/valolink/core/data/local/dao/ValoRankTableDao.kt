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

@Dao
interface ValoRankTableDao {
    /**
     * Inserts or updates the provided rank table entities.
     *
     * @param rankTables The rank table entities to insert or update.
     */
    @Upsert
    suspend fun upsert(rankTables: List<ValoRankTableEntity>)

    /**
     * Retrieves a rank table entity by UUID.
     *
     * @return A Flow emitting the matching `ValoRankTableEntity`, or `null` if no entity exists for the given UUID.
     */
    @Query("SELECT * FROM valo_rank_tables WHERE uuid = :uuid")
    fun get(uuid: String): Flow<ValoRankTableEntity?>

    /**
     * Retrieves all rank table entities.
     *
     * @return A Flow emitting the list of all rank table entities.
     */
    @Query("SELECT * FROM valo_rank_tables")
    fun get(): Flow<List<ValoRankTableEntity>>
}