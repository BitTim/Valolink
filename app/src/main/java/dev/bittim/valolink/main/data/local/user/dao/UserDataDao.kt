package dev.bittim.valolink.main.data.local.user.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.main.data.local.user.entity.UserDataEntity
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

    @Query("SELECT * FROM Users WHERE isSynced = false ORDER BY updatedAt ASC")
    fun getSyncQueue(): Flow<List<UserDataEntity?>>

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