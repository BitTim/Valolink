/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserDataDao.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   04.05.25, 10:54
 */

package dev.bittim.valolink.user.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.user.data.local.UserDatabase
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

    @Transaction
    @Query("SELECT * FROM Users WHERE uuid = :uuid LIMIT 1")
    fun getByUuid(uuid: String): Flow<UserMetaEntity?>

    @Query("SELECT updatedAt FROM Users WHERE uuid = :uuid LIMIT 1")
    fun getUpdatedAtByUuid(uuid: String): Flow<String?>

    @Query("SELECT * FROM Users WHERE isSynced = false AND uuid != :localUser ORDER BY updatedAt ASC")
    fun getSyncQueue(localUser: String = UserDatabase.LOCAL_UUID): Flow<List<UserMetaEntity?>>

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
