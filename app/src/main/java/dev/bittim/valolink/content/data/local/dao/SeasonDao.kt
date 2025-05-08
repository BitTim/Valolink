/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       SeasonDao.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.content.data.local.entity.SeasonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SeasonDao {
	// --------------------------------
	//  Upsert
	// --------------------------------

	@Upsert
	suspend fun upsert(season: SeasonEntity)

	@Upsert
	suspend fun upsert(seasons: List<SeasonEntity>)

	// --------------------------------
	//  Query
	// --------------------------------

	@Query("SELECT * FROM Seasons WHERE uuid = :uuid LIMIT 1")
	fun getByUuid(uuid: String): Flow<SeasonEntity?>

	@Query("SELECT * FROM Seasons ORDER BY startTime DESC")
	fun getAll(): Flow<List<SeasonEntity>>
}