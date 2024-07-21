package dev.bittim.valolink.main.data.local.user.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.main.data.local.user.entity.ProgressionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GearDao {
    // --------------------------------
    //  Upsert
    // --------------------------------

    @Upsert
    suspend fun upsert(gear: ProgressionEntity)

    @Transaction
    @Upsert
    suspend fun upsert(gears: Set<ProgressionEntity>)

    // --------------------------------
    //  Query
    // --------------------------------

    @Query("SELECT * FROM Gears WHERE uuid = :uuid LIMIT 1")
    fun getByUuid(uuid: String): Flow<ProgressionEntity?>

    @Query("SELECT * FROM Gears WHERE user = :uid")
    fun getByUser(uid: String): Flow<List<ProgressionEntity>>

    @Query("SELECT * FROM Gears WHERE user = :uid AND contract = :contract LIMIT 1")
    fun getByUserAndContract(
        uid: String,
        contract: String,
    ): Flow<ProgressionEntity?>

    @Query("SELECT updatedAt FROM Gears WHERE uuid = :uuid LIMIT 1")
    fun getUpdatedAtByUuid(uuid: String): Flow<String?>

    @Query("SELECT * FROM Gears WHERE isSynced = false ORDER BY updatedAt ASC")
    fun getSyncQueue(): Flow<List<ProgressionEntity?>>

    // --------------------------------
    //  Delete
    // --------------------------------

    @Delete
    suspend fun delete(gear: ProgressionEntity)

    @Query("DELETE FROM Gears WHERE uuid = :uuid")
    suspend fun deleteByUuid(uuid: String)

    @Query("DELETE FROM Gears WHERE user = :uid")
    suspend fun deleteByUser(uid: String)

    @Query("DELETE FROM Gears")
    suspend fun deleteAll()
}