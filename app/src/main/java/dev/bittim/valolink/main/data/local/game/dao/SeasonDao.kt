package dev.bittim.valolink.main.data.local.game.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.main.data.local.game.entity.SeasonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SeasonDao {
    // --------------------------------
    //  Upsert
    // --------------------------------

    @Upsert
    suspend fun upsertSeason(season: SeasonEntity)

    @Upsert
    suspend fun upsertAllSeasons(seasons: List<SeasonEntity>)

    // --------------------------------
    //  Query
    // --------------------------------

    @Query("SELECT * FROM Seasons WHERE uuid = :uuid LIMIT 1")
    fun getSeason(uuid: String): Flow<SeasonEntity?>

    @Query("SELECT * FROM Seasons ORDER BY startTime DESC")
    fun getAllSeasons(): Flow<List<SeasonEntity>>
}