package dev.bittim.valolink.feature.content.data.local.game

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.feature.content.data.local.game.entity.SeasonEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContractEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Upsert
    suspend fun upsertAllSeasons(seasons: List<SeasonEntity>)
    @Upsert
    suspend fun upsertAllContracts(seasons: List<ContractEntity>)

    @Query("SELECT * FROM seasonentity ORDER BY endTime ASC")
    fun getAllSeasons(): Flow<List<SeasonEntity>>

    @Query("SELECT * FROM contractentity")
    fun getAllContracts(): Flow<List<ContractEntity>>

    @Query("DELETE FROM seasonentity")
    suspend fun clearAllSeasons()

    @Query("DELETE FROM contractentity")
    suspend fun clearAllContracts()
}