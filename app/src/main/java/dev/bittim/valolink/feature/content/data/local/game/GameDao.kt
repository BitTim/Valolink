package dev.bittim.valolink.feature.content.data.local.game

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.feature.content.data.local.game.entity.MapEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.SeasonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Upsert
    suspend fun upsertAllSeasons(seasons: List<SeasonEntity>)

    @Upsert
    suspend fun upsertAllMaps(maps: List<MapEntity>)

    @Query("SELECT * FROM seasonentity ORDER BY endTime ASC")
    fun getAllSeasons(): Flow<List<SeasonEntity>>

    @Query("SELECT * FROM mapentity ORDER BY displayName ASC")
    fun getAllMaps(): Flow<List<MapEntity>>

    @Query("DELETE FROM seasonentity")
    suspend fun clearAllSeasons()

    @Query("DELETE FROM mapentity")
    suspend fun clearAllMaps()
}