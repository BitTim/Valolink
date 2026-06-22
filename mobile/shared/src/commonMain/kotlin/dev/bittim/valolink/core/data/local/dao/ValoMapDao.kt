/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoMapDao.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   08.06.26, 20:08
 */

package dev.bittim.valolink.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.core.data.local.entity.ValoMapEntity
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface ValoMapDao {
    @Upsert
    suspend fun upsert(valoMapEntities: List<ValoMapEntity>)

    @Query("SELECT * FROM valo_maps WHERE uuid = :uuid LIMIT 1")
    fun get(uuid: Uuid): Flow<ValoMapEntity?>

    @Query("SELECT * FROM valo_maps")
    fun get(): Flow<List<ValoMapEntity>>
}