/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserRankDao.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   20.04.25, 03:29
 */

package dev.bittim.valolink.user.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.user.data.local.UserDatabase
import dev.bittim.valolink.user.data.local.entity.UserRankEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserRankDao {
    // --------------------------------
    //  Upsert
    // --------------------------------

    @Upsert
    suspend fun upsert(rank: UserRankEntity)

    @Transaction
    @Upsert
    suspend fun upsert(ranks: Set<UserRankEntity>)

    // --------------------------------
    //  Query
    // --------------------------------

    @Query("SELECT * FROM UserRanks WHERE uuid = :uuid LIMIT 1")
    fun getByUuid(uuid: String): Flow<UserRankEntity?>

    @Query("SELECT * FROM UserRanks WHERE user = :uid LIMIT 1")
    fun getByUser(uid: String): Flow<UserRankEntity?>

    @Query("SELECT updatedAt FROM UserRanks WHERE uuid = :uuid LIMIT 1")
    fun getUpdatedAtByUuid(uuid: String): Flow<String?>

    @Query("SELECT updatedAt FROM UserRanks WHERE user = :uid LIMIT 1")
    fun getUpdatedAtByUser(uid: String): Flow<String?>

    @Query("SELECT * FROM UserRanks WHERE isSynced = false AND user != :localUser ORDER BY updatedAt ASC")
    fun getSyncQueue(localUser: String = UserDatabase.LOCAL_UUID): Flow<List<UserRankEntity?>>

    // --------------------------------
    //  Delete
    // --------------------------------

    @Delete
    suspend fun delete(agent: UserRankEntity)

    @Query("DELETE FROM UserRanks WHERE uuid = :uuid")
    suspend fun deleteByUuid(uuid: String)

    @Query("DELETE FROM UserRanks WHERE user = :uid")
    suspend fun deleteByUser(uid: String)

    @Query("DELETE FROM UserRanks")
    suspend fun deleteAll()
}
