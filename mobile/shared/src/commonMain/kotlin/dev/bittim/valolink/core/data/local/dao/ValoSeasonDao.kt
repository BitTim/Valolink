/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoSeasonDao.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   22.06.26, 04:12
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
    fun get(uuid: Uuid): Flow<ValoSeasonEntity?>

    @Query("SELECT * FROM valo_seasons WHERE startTime < :time AND :time < endTime LIMIT 1")
    fun get(time: Instant): Flow<ValoSeasonEntity?>

    @Query("SELECT * FROM valo_seasons")
    fun get(): Flow<List<ValoSeasonEntity>>
}