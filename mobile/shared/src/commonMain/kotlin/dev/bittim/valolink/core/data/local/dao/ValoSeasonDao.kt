/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoSeasonDao.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 16:33
 */

package dev.bittim.valolink.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.core.data.local.entity.ValoSeasonEntity
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Dao
interface ValoSeasonDao {
    @Upsert
    suspend fun upsert(seasons: List<ValoSeasonEntity>)

    @Query("SELECT * FROM valo_seasons WHERE uuid = :uuid LIMIT 1")
    fun observe(uuid: Uuid): Flow<ValoSeasonEntity?>

    @Query("SELECT * FROM valo_seasons WHERE startTime < :time AND :time < endTime LIMIT 1")
    fun observe(time: Instant): Flow<ValoSeasonEntity?>

    @Query("SELECT * FROM valo_seasons")
    fun observe(): Flow<List<ValoSeasonEntity>>
}