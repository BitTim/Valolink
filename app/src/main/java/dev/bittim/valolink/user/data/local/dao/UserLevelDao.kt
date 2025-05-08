/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserLevelDao.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.05.25, 12:35
 */

package dev.bittim.valolink.user.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.user.data.local.entity.UserLevelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserLevelDao {
    // --------------------------------
    //  Upsert
    // --------------------------------

    @Upsert
    suspend fun upsert(userLevel: UserLevelEntity)

    @Transaction
    @Upsert
    suspend fun upsert(userLevels: Set<UserLevelEntity>)

    // --------------------------------
    //  Query
    // --------------------------------

    @Query("SELECT * FROM UserLevels WHERE uuid = :uuid LIMIT 1")
    fun getByUuid(uuid: String): Flow<UserLevelEntity?>

    @Query("SELECT * FROM UserLevels WHERE userContract = :userContract")
    fun getByContract(userContract: String): Flow<List<UserLevelEntity>>

    @Query("SELECT * FROM UserLevels WHERE userContract = :userContract AND level = :level LIMIT 1")
    fun getByContractAndLevel(
        userContract: String,
        level: String,
    ): Flow<UserLevelEntity?>

    @Query("SELECT updatedAt FROM UserLevels WHERE uuid = :uuid LIMIT 1")
    fun getUpdatedAtByUuid(uuid: String): Flow<String?>

    @Query("SELECT * FROM UserLevels WHERE isSynced = false ORDER BY updatedAt ASC")
    fun getSyncQueue(): Flow<List<UserLevelEntity?>>

    @Query("SELECT * FROM UserLevels WHERE toDelete = true ORDER BY updatedAt ASC")
    fun getDeleteQueue(): Flow<List<UserLevelEntity?>>

    // --------------------------------
    //  Delete
    // --------------------------------

    @Delete
    suspend fun delete(userLevels: UserLevelEntity)

    @Query("DELETE FROM UserLevels WHERE uuid = :uuid")
    suspend fun deleteByUuid(uuid: String)

    @Query("DELETE FROM UserLevels WHERE userContract = :userContract AND level = :level")
    fun deleteByContractAndLevel(userContract: String, level: String)

    @Query("DELETE FROM UserLevels WHERE userContract = :userContract")
    suspend fun deleteByContract(userContract: String)

    @Query("DELETE FROM UserLevels")
    suspend fun deleteAll()
}
