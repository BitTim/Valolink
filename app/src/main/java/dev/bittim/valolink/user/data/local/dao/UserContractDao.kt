/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserContractDao.kt
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
import dev.bittim.valolink.user.data.local.entity.UserContractEntity
import dev.bittim.valolink.user.data.local.relation.UserContractWithLevels
import kotlinx.coroutines.flow.Flow

@Dao
interface UserContractDao {
    // --------------------------------
    //  Upsert
    // --------------------------------

    @Upsert
    suspend fun upsert(userContract: UserContractEntity)

    @Transaction
    @Upsert
    suspend fun upsert(userContracts: Set<UserContractEntity>)

    // --------------------------------
    //  Query
    // --------------------------------

    @Transaction
    @Query("SELECT * FROM UserContracts WHERE uuid = :uuid LIMIT 1")
    fun getByUuid(uuid: String): Flow<UserContractWithLevels?>

    @Transaction
    @Query("SELECT * FROM UserContracts WHERE user = :uid")
    fun getByUser(uid: String): Flow<List<UserContractWithLevels>>

    @Transaction
    @Query("SELECT * FROM UserContracts WHERE user = :uid AND contract = :contract LIMIT 1")
    fun getByUserAndContract(
        uid: String,
        contract: String,
    ): Flow<UserContractWithLevels?>

    @Query("SELECT updatedAt FROM UserContracts WHERE uuid = :uuid LIMIT 1")
    fun getUpdatedAtByUuid(uuid: String): Flow<String?>

    @Transaction
    @Query("SELECT * FROM UserContracts WHERE isSynced = false ORDER BY updatedAt ASC")
    fun getSyncQueue(): Flow<List<UserContractWithLevels?>>

    @Transaction
    @Query("SELECT * FROM UserContracts WHERE toDelete = true ORDER BY updatedAt ASC")
    fun getDeleteQueue(): Flow<List<UserContractWithLevels?>>

    // --------------------------------
    //  Delete
    // --------------------------------

    @Delete
    suspend fun delete(userContracts: UserContractEntity)

    @Query("DELETE FROM UserContracts WHERE uuid = :uuid")
    suspend fun deleteByUuid(uuid: String)

    @Query("DELETE FROM UserContracts WHERE user = :uid AND contract = :contract")
    fun deleteByUserAndContract(uid: String, contract: String)

    @Query("DELETE FROM UserContracts WHERE user = :uid")
    suspend fun deleteByUser(uid: String)

    @Query("DELETE FROM UserContracts")
    suspend fun deleteAll()
}
