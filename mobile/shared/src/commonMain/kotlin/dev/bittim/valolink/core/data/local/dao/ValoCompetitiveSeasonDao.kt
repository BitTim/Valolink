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
    @Upsert
    suspend fun upsert(competitiveSeasons: List<ValoCompetitiveSeasonEntity>)

    @Query("SELECT * FROM valo_competitive_seasons WHERE uuid = :uuid LIMIT 1")
    fun get(uuid: Uuid): Flow<ValoCompetitiveSeasonEntity?>

    @Query("SELECT * FROM valo_competitive_seasons WHERE season = :season LIMIT 1")
    fun getBySeason(season: Uuid): Flow<ValoCompetitiveSeasonEntity?>

    @Query("SELECT * FROM valo_competitive_seasons")
    fun get(): Flow<List<ValoCompetitiveSeasonEntity>>
}