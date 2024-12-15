/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       EventDao.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.content.data.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
	// --------------------------------
	//  Upsert
	// --------------------------------

	@Upsert
	suspend fun upsert(event: EventEntity)

	@Upsert
	suspend fun upsert(events: Set<EventEntity>)

	// --------------------------------
	//  Query
	// --------------------------------

	@Query("SELECT * FROM Events WHERE uuid = :uuid LIMIT 1")
	fun getByUuid(uuid: String): Flow<EventEntity?>

	@Query("SELECT * FROM Events ORDER BY startTime DESC")
	fun getAll(): Flow<List<EventEntity>>
}