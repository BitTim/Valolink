package dev.bittim.valolink.main.data.local.game.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.main.data.local.game.entity.PlayerTitleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerTitleDao {
    // --------------------------------
    //  Upsert
    // --------------------------------

    @Upsert
    suspend fun upsert(playerTitle: PlayerTitleEntity)

    // --------------------------------
    //  Query
    // --------------------------------

    @Query("SELECT * FROM PlayerTitles WHERE uuid = :uuid LIMIT 1")
    fun getByUuid(uuid: String): Flow<PlayerTitleEntity?>
}