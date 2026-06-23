/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoCompetitiveSeasonDao.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.06.26, 03:03
 */

package dev.bittim.valolink.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.core.data.local.entity.ValoCompetitiveSeasonEntity
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface ValoCompetitiveSeasonDao {
    /**
     * Inserts or updates the provided competitive seasons.
     */
    @Upsert
    suspend fun upsert(competitiveSeasons: List<ValoCompetitiveSeasonEntity>)

    /**
     * Retrieves the competitive season for the given UUID.
     *
     * @return The competitive season entity if found, `null` otherwise.
     */
    @Query("SELECT * FROM valo_competitive_seasons WHERE uuid = :uuid LIMIT 1")
    fun get(uuid: Uuid): Flow<ValoCompetitiveSeasonEntity?>

    /**
     * Retrieves the competitive season entity for the given season ID.
     *
     * @param season The season ID to match.
     * @return The competitive season entity matching the given season ID, or null if not found.
     */
    @Query("SELECT * FROM valo_competitive_seasons WHERE season = :season LIMIT 1")
    fun getBySeason(season: Uuid): Flow<ValoCompetitiveSeasonEntity?>

    /**
     * Retrieves all competitive season entities.
     *
     * @return A flow emitting the list of all competitive season entities whenever table contents change.
     */
    @Query("SELECT * FROM valo_competitive_seasons")
    fun get(): Flow<List<ValoCompetitiveSeasonEntity>>
}