/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserDataDao.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.04.25, 02:40
 */

package dev.bittim.valolink.user.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.user.data.local.UserDatabase
import dev.bittim.valolink.user.data.local.entity.UserDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDataDao {
    // --------------------------------
    //  Upsert
    // --------------------------------

    @Upsert
    suspend fun upsert(userData: UserDataEntity)

    // --------------------------------
    //  Queries
    // --------------------------------

    @Transaction
    @Query("SELECT * FROM Users WHERE uuid = :uuid LIMIT 1")
    fun getByUuid(uuid: String): Flow<UserDataEntity?>

    @Query("SELECT updatedAt FROM Users WHERE uuid = :uuid LIMIT 1")
    fun getUpdatedAtByUuid(uuid: String): Flow<String?>

    @Query("SELECT * FROM Users WHERE isSynced = false AND uuid != :localUser ORDER BY updatedAt ASC")
    fun getSyncQueue(localUser: String = UserDatabase.LOCAL_UUID): Flow<List<UserDataEntity?>>

    // --------------------------------
    //  Delete
    // --------------------------------

    @Delete
    suspend fun delete(userData: UserDataEntity)

    @Query("DELETE FROM Users WHERE uuid = :uuid")
    suspend fun deleteByUuid(uuid: String)

    @Transaction
    @Query("DELETE FROM Users")
    suspend fun deleteAll()
}
