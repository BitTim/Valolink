package dev.bittim.valolink.feature.main.data.local.game.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.feature.main.data.local.game.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    // --------------------------------
    //  Upsert
    // --------------------------------

    @Upsert
    suspend fun upsertEvent(event: EventEntity)



    @Upsert
    suspend fun upsertAllEvents(seasons: List<EventEntity>)

    // --------------------------------
    //  Query
    // --------------------------------

    @Query("SELECT * FROM Events WHERE uuid = :uuid LIMIT 1")
    fun getEvent(uuid: String): Flow<EventEntity?>



    @Query("SELECT * FROM Events ORDER BY startTime DESC")
    fun getAllEvents(): Flow<List<EventEntity>>
}