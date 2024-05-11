package dev.bittim.valolink.feature.content.data.local.game.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.feature.content.data.local.game.entity.SprayEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SprayDao {
    // --------------------------------
    //  Upsert
    // --------------------------------

    @Upsert
    suspend fun upsertSpray(spray: SprayEntity)

    // --------------------------------
    //  Query
    // --------------------------------

    @Query("SELECT * FROM Sprays WHERE uuid = :uuid LIMIT 1")
    fun getSpray(uuid: String): Flow<SprayEntity?>
}