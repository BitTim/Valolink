/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoVersionDao.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   06.06.26, 13:53
 */

package dev.bittim.valolink.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.core.data.local.entity.ValoVersionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ValoVersionDao {
    @Upsert
    suspend fun upsert(version: ValoVersionEntity)

    @Query("SELECT * FROM valo_version LIMIT 1")
    fun observe(): Flow<ValoVersionEntity?>
}