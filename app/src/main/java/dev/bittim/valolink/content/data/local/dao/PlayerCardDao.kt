/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       PlayerCardDao.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.content.data.local.entity.PlayerCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerCardDao {
	// --------------------------------
	//  Upsert
	// --------------------------------

	@Upsert
	suspend fun upsert(playerCard: PlayerCardEntity)

	@Transaction
	@Upsert
	suspend fun upsert(playerCards: Set<PlayerCardEntity>)

	// --------------------------------
	//  Query
	// --------------------------------

	@Query("SELECT * FROM PlayerCards WHERE uuid = :uuid LIMIT 1")
	fun getByUuid(uuid: String): Flow<PlayerCardEntity?>

	@Query("SELECT * FROM PlayerCards")
	fun getAll(): Flow<List<PlayerCardEntity>>
}