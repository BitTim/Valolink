/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       FlexDao.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   20.04.25, 03:29
 */

package dev.bittim.valolink.content.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.content.data.local.entity.FlexEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlexDao {
    // --------------------------------
    //  Upsert
    // --------------------------------

    @Upsert
    suspend fun upsert(flex: FlexEntity)

    @Transaction
    @Upsert
    suspend fun upsert(flexes: Set<FlexEntity>)

    // --------------------------------
    //  Queries
    // --------------------------------

    @Query("SELECT * FROM Flexes WHERE uuid = :uuid LIMIT 1")
    fun getByUuid(uuid: String): Flow<FlexEntity?>

    @Query("SELECT * FROM Flexes")
    fun getAll(): Flow<List<FlexEntity>>
}
