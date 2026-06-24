/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoRankDao.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.06.26, 04:06
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
    /**
     * Inserts or updates the provided rank entities.
     *
     * @param ranks The rank entities to insert or update.
     */
    @Upsert
    suspend fun upsert(ranks: List<ValoRankEntity>)

    /**
     * Retrieves the rank entity matching a specific rank table and tier.
     *
     * @param rankTable The UUID of the rank table to filter by.
     * @param tier The tier level to filter by.
     * @return A Flow emitting the matching ValoRankEntity, or null if no match exists.
     */
    @Query("SELECT * FROM valo_ranks WHERE rankTable = :rankTable AND tier = :tier LIMIT 1")
    fun get(rankTable: Uuid, tier: Int): Flow<ValoRankEntity?>

    /**
     * Fetches all rank entities for a specific rank table.
     *
     * @param rankTable The unique identifier of the rank table.
     * @return A stream emitting lists of rank entities for the specified rank table.
     */
    @Query("SELECT * FROM valo_ranks WHERE rankTable = :rankTable ORDER BY tier ASC")
    fun get(rankTable: Uuid): Flow<List<ValoRankEntity>>

    /**
     * Retrieves all rank entities from the database.
     *
     * @return A Flow that emits lists of all rank entities.
     */
    @Query("SELECT * FROM valo_ranks")
    fun get(): Flow<List<ValoRankEntity>>
}