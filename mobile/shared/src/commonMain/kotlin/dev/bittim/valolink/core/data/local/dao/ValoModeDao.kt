/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoModeDao.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 16:31
 */

package dev.bittim.valolink.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.core.data.local.entity.ValoModeEntity
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

private const val singleQuery = "SELECT * FROM valo_modes WHERE uuid = :uuid LIMIT 1"
private const val allQuery = "SELECT * FROM valo_modes"

@Dao
interface ValoModeDao {
    @Upsert
    suspend fun upsert(valoModes: List<ValoModeEntity>)

    @Query(singleQuery)
    suspend fun get(uuid: Uuid): ValoModeEntity?

    @Query(singleQuery)
    fun observe(uuid: Uuid): Flow<ValoModeEntity?>

    @Query(allQuery)
    fun observe(): Flow<List<ValoModeEntity>>
}