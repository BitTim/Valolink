/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoMapDao.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 16:30
 */

package dev.bittim.valolink.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.core.data.local.entity.ValoMapEntity
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

private const val singleQuery = "SELECT * FROM valo_maps WHERE uuid = :uuid LIMIT 1"
private const val allQuery = "SELECT * FROM valo_maps"

@Dao
interface ValoMapDao {

    @Upsert
    suspend fun upsert(valoMapEntities: List<ValoMapEntity>)

    @Query(singleQuery)
    suspend fun get(uuid: Uuid): ValoMapEntity?

    @Query(singleQuery)
    fun observe(uuid: Uuid): Flow<ValoMapEntity?>

    @Query(allQuery)
    fun observe(): Flow<List<ValoMapEntity>>
}