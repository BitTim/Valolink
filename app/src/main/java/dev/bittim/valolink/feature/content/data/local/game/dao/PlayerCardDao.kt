package dev.bittim.valolink.feature.content.data.local.game.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.feature.content.data.local.game.entity.PlayerCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerCardDao {
    // --------------------------------
    //  Upsert
    // --------------------------------

    @Upsert
    suspend fun upsertPlayerCard(playerCard: PlayerCardEntity)

    // --------------------------------
    //  Query
    // --------------------------------

    @Query("SELECT * FROM PlayerCards WHERE uuid = :uuid LIMIT 1")
    fun getPlayerCard(uuid: String): Flow<PlayerCardEntity?>
}