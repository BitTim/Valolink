/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserMetaDao.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.05.25, 13:02
 */

package dev.bittim.valolink.user.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.user.data.local.entity.UserMetaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserMetaDao {
    // --------------------------------
    //  Upsert
    // --------------------------------

    @Upsert
    suspend fun upsert(userData: UserMetaEntity)

    // --------------------------------
    //  Queries
    // --------------------------------

    @Query("SELECT * FROM Users WHERE uuid = :uuid LIMIT 1")
    fun getByUuid(uuid: String): Flow<UserMetaEntity?>

    @Query("SELECT updatedAt FROM Users WHERE uuid = :uuid LIMIT 1")
    fun getUpdatedAtByUuid(uuid: String): Flow<String?>

    @Query("SELECT * FROM Users WHERE isSynced = false ORDER BY updatedAt ASC")
    fun getSyncQueue(): Flow<List<UserMetaEntity?>>

    @Query("SELECT * FROM Users WHERE toDelete = true ORDER BY updatedAt ASC")
    fun getDeleteQueue(): Flow<List<UserMetaEntity?>>

    // --------------------------------
    //  Delete
    // --------------------------------

    @Delete
    suspend fun delete(userData: UserMetaEntity)

    @Query("DELETE FROM Users WHERE uuid = :uuid")
    suspend fun deleteByUuid(uuid: String)

    @Transaction
    @Query("DELETE FROM Users")
    suspend fun deleteAll()
}
