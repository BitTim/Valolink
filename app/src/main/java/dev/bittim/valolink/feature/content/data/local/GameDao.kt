package dev.bittim.valolink.feature.content.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Upsert
    suspend fun upsertAll(seasons: List<SeasonEntity>)

    @Query("SELECT * FROM seasonentity ORDER BY endTime ASC")
    fun getAllSeasons(): Flow<List<SeasonEntity>>

    @Query("DELETE FROM seasonentity")
    suspend fun clearAll()
}