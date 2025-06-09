/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       MapDao.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.content.data.local.entity.map.MapCalloutEntity
import dev.bittim.valolink.content.data.local.entity.map.MapEntity
import dev.bittim.valolink.content.data.local.relation.MapWithCallouts
import kotlinx.coroutines.flow.Flow

@Dao
interface MapDao {
    // --------------------------------
    //  Upsert
    // --------------------------------

    @Transaction
    @Upsert
    suspend fun upsert(
        map: MapEntity,
        callouts: Set<MapCalloutEntity>,
    )

    @Transaction
    @Upsert
    suspend fun upsert(
        map: Set<MapEntity>,
        callouts: Set<MapCalloutEntity>,
    )

    // --------------------------------
    //  Query
    // --------------------------------

    @Transaction
    @Query("SELECT * FROM Maps WHERE uuid = :uuid LIMIT 1")
    fun getByUuid(uuid: String): Flow<MapWithCallouts?>

    @Transaction
    @Query("SELECT * FROM Maps")
    fun getAll(): Flow<List<MapWithCallouts>>
}