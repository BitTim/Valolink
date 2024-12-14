/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       BuddyDao.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.content.data.local.entity.buddy.BuddyEntity
import dev.bittim.valolink.content.data.local.entity.buddy.BuddyLevelEntity
import dev.bittim.valolink.content.data.local.relation.buddy.BuddyWithLevels
import kotlinx.coroutines.flow.Flow

@Dao
interface BuddyDao {
	// --------------------------------
	//  Upsert
	// --------------------------------

	@Transaction
	@Upsert
	suspend fun upsert(
		buddy: BuddyEntity,
		levels: Set<BuddyLevelEntity>,
	)

	@Transaction
	@Upsert
	suspend fun upsert(
		buddy: Set<BuddyEntity>,
		levels: Set<BuddyLevelEntity>,
	)

	// --------------------------------
	//  Query
	// --------------------------------

	@Transaction
	@Query("SELECT * FROM Buddies")
	fun getAll(): Flow<List<BuddyWithLevels>>

	@Transaction
	@Query("SELECT * FROM Buddies WHERE uuid = :uuid LIMIT 1")
	fun getByUuid(uuid: String): Flow<BuddyWithLevels?>

	@Transaction
	@Query(
		"""
        SELECT * FROM Buddies
        WHERE uuid = (
            SELECT buddy FROM BuddyLevels
            WHERE uuid = :levelUuid
        ) LIMIT 1
    """
	)
	fun getByLevelUuid(levelUuid: String): Flow<BuddyWithLevels?>
}