package dev.bittim.valolink.feature.content.data.local.game

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.feature.content.data.local.game.entity.EventEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.SeasonEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.agent.AbilityEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.agent.AgentEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.agent.RecruitmentEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.agent.RoleEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ChapterEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContentEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContractEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.LevelEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.RewardEntity
import dev.bittim.valolink.feature.content.data.local.game.relation.agent.AgentWithRecruitmentAndRoleAndAbilities
import dev.bittim.valolink.feature.content.data.local.game.relation.contract.ContractWithChaptersWithLevelsAndRewards
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Upsert
    suspend fun upsertSeason(season: SeasonEntity)
    @Upsert
    suspend fun upsertAllSeasons(seasons: List<SeasonEntity>)



    @Upsert
    suspend fun upsertEvent(event: EventEntity)



    @Upsert
    suspend fun upsertAllEvents(seasons: List<EventEntity>)



    @Transaction
    @Upsert
    suspend fun upsertAllContracts(
        contracts: Set<ContractEntity>,
        contents: Set<ContentEntity>,
        chapters: Set<ChapterEntity>,
        levels: Set<LevelEntity>,
        rewards: Set<RewardEntity>
    )



    @Transaction
    @Upsert
    suspend fun upsertAgent(
        role: RoleEntity, agent: AgentEntity, abilities: Set<AbilityEntity>
    )



    @Upsert
    suspend fun upsertAgentRecruitment(
        recruitment: RecruitmentEntity
    )



    @Transaction
    @Upsert
    suspend fun upsertAllAgents(
        roles: Set<RoleEntity>,
        agents: Set<AgentEntity>,
        recruitments: Set<RecruitmentEntity>,
        abilities: Set<AbilityEntity>
    )



    @Query("SELECT * FROM Seasons WHERE uuid = :uuid LIMIT 1")
    fun getSeason(uuid: String): Flow<SeasonEntity?>



    @Query("SELECT * FROM Seasons ORDER BY endTime DESC")
    fun getAllSeasons(): Flow<List<SeasonEntity>>



    @Query("SELECT * FROM Events WHERE uuid = :uuid LIMIT 1")
    fun getEvent(uuid: String): Flow<EventEntity?>



    @Query("SELECT * FROM Events ORDER BY endTime DESC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Transaction
    @Query("SELECT * FROM Contracts")
    fun getAllContracts(): Flow<List<ContractWithChaptersWithLevelsAndRewards>>



    @Transaction
    @Query("SELECT * FROM Agents WHERE uuid = :uuid LIMIT 1")
    fun getAgent(uuid: String): Flow<AgentWithRecruitmentAndRoleAndAbilities?>



    @Transaction
    @Query("SELECT * FROM Agents")
    fun getAllAgents(): Flow<List<AgentWithRecruitmentAndRoleAndAbilities>>



    @Query("DELETE FROM Seasons")
    suspend fun clearAllSeasons()



    @Query("DELETE FROM Contracts")
    suspend fun clearAllContracts()
}