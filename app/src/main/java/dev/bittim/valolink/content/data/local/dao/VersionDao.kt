/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       VersionDao.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.content.data.local.entity.VersionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VersionDao {
	// --------------------------------
	//  Upsert
	// --------------------------------

	@Upsert
	suspend fun upsert(version: VersionEntity)

	// --------------------------------
	//  Query
	// --------------------------------

	@Query("SELECT * FROM Version WHERE id = 0 LIMIT 1")
	fun get(): Flow<VersionEntity?>
}