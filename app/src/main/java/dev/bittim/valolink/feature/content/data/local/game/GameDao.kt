package dev.bittim.valolink.feature.content.data.local.game

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.feature.content.data.local.game.entity.SeasonEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ChapterEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ChapterLevelEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContractContentEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContractEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.RewardEntity
import dev.bittim.valolink.feature.content.data.local.game.relation.contract.ContractWithChaptersWithLevelsAndRewards
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Upsert
    suspend fun upsertAllSeasons(seasons: List<SeasonEntity>)
    @Upsert
    suspend fun upsertAllContracts(
        contracts: List<ContractEntity>,
        contents: List<ContractContentEntity>,
        chapters: List<ChapterEntity>,
        levels: List<ChapterLevelEntity>,
        rewards: List<RewardEntity>
    )

    @Query("SELECT * FROM seasonentity ORDER BY endTime ASC")
    fun getAllSeasons(): Flow<List<SeasonEntity>>

    @Transaction
    @Query("SELECT * FROM contracts")
    fun getAllContracts(): Flow<List<ContractWithChaptersWithLevelsAndRewards>>

    @Query("DELETE FROM seasonentity")
    suspend fun clearAllSeasons()

    @Query("DELETE FROM contracts")
    suspend fun clearAllContracts()
}