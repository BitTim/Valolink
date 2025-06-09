/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ModeDao.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.content.data.local.ModeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ModeDao {
    // --------------------------------
    //  Upsert
    // --------------------------------

    @Upsert
    suspend fun upsert(mode: ModeEntity)

    @Upsert
    suspend fun upsert(modes: Set<ModeEntity>)

    // --------------------------------
    //  Query
    // --------------------------------

    @Query("SELECT * FROM Modes WHERE uuid = :uuid LIMIT 1")
    fun getByUuid(uuid: String): Flow<ModeEntity?>

    @Query("SELECT * FROM Modes")
    fun getAll(): Flow<List<ModeEntity>>
}