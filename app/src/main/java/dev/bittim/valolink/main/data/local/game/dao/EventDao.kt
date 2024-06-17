package dev.bittim.valolink.main.data.local.game.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.main.data.local.game.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    // --------------------------------
    //  Upsert
    // --------------------------------

    @Upsert
    suspend fun upsert(event: EventEntity)

    @Upsert
    suspend fun upsert(events: Set<EventEntity>)

    // --------------------------------
    //  Query
    // --------------------------------

    @Query("SELECT * FROM Events WHERE uuid = :uuid LIMIT 1")
    fun getByUuid(uuid: String): Flow<EventEntity?>

    @Query("SELECT * FROM Events ORDER BY startTime DESC")
    fun getAll(): Flow<List<EventEntity>>
}