/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoModeDao.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   08.06.26, 20:13
 */

package dev.bittim.valolink.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.core.data.local.entity.ValoModeEntity
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface ValoModeDao {
    @Upsert
    suspend fun upsert(valoModes: List<ValoModeEntity>)

    @Query("SELECT * FROM valo_modes WHERE uuid = :uuid LIMIT 1")
    fun get(uuid: Uuid): Flow<ValoModeEntity?>

    @Query("SELECT * FROM valo_modes")
    fun get(): Flow<List<ValoModeEntity>>
}