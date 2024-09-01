package dev.bittim.valolink.main.data.local.game.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.main.data.local.game.entity.agent.RecruitmentEntity
import dev.bittim.valolink.main.data.local.game.entity.contract.ChapterEntity
import dev.bittim.valolink.main.data.local.game.entity.contract.ContentEntity
import dev.bittim.valolink.main.data.local.game.entity.contract.ContractEntity
import dev.bittim.valolink.main.data.local.game.entity.contract.LevelEntity
import dev.bittim.valolink.main.data.local.game.entity.contract.RewardEntity
import dev.bittim.valolink.main.data.local.game.relation.agent.RecruitmentWithAgentWithRoleAndAbilities
import dev.bittim.valolink.main.data.local.game.relation.contract.ChapterWithLevelsAndRewards
import dev.bittim.valolink.main.data.local.game.relation.contract.ContentWithChaptersWithLevelsAndRewards
import dev.bittim.valolink.main.data.local.game.relation.contract.ContractWithContentWithChaptersWithLevelsAndRewards
import dev.bittim.valolink.main.data.local.game.relation.contract.LevelWithReward
import kotlinx.coroutines.flow.Flow

@Dao
interface ContractsDao {
    // --------------------------------
    //  Upsert
    // --------------------------------

    @Transaction
    @Upsert
    suspend fun upsert(
        contract: ContractEntity,
        content: ContentEntity,
        chapters: Set<ChapterEntity>,
        levels: Set<LevelEntity>,
        rewards: Set<RewardEntity>,
    )

    @Upsert
    suspend fun upsertRecruitment(
        recruitment: RecruitmentEntity,
    )

    @Transaction
    @Upsert
    suspend fun upsert(
        contracts: Set<ContractEntity>,
        contents: Set<ContentEntity>,
        chapters: Set<ChapterEntity>,
        levels: Set<LevelEntity>,
        rewards: Set<RewardEntity>,
    )

    @Transaction
    @Upsert
    suspend fun upsertRecruitment(
        recruitments: Set<RecruitmentEntity>,
    )

    // --------------------------------
    //  Queries
    // --------------------------------

    // -------- [ Single Object ] --------

    @Transaction
    @Query("SELECT * FROM Contracts WHERE uuid = :uuid LIMIT 1")
    fun getByUuid(uuid: String): Flow<ContractWithContentWithChaptersWithLevelsAndRewards?>

    @Transaction
    @Query("SELECT * FROM ContractContents WHERE uuid = :uuid LIMIT 1")
    fun getContentByUuid(uuid: String): Flow<ContentWithChaptersWithLevelsAndRewards?>

    @Transaction
    @Query("SELECT * FROM ContractChapters WHERE uuid = :uuid LIMIT 1")
    fun getChapterByUuid(uuid: String): Flow<ChapterWithLevelsAndRewards?>

    @Transaction
    @Query("SELECT * FROM ContractChapterLevels WHERE uuid = :uuid LIMIT 1")
    fun getLevelByUuid(uuid: String): Flow<LevelWithReward?>

    @Transaction
    @Query("SELECT * FROM ContractChapterLevels WHERE dependency = :dependency LIMIT 1")
    fun getLevelByDependency(dependency: String): Flow<LevelWithReward?>

    @Transaction
    @Query("SELECT * FROM AgentRecruitments WHERE uuid = :uuid LIMIT 1")
    fun getRecruitmentByUuid(uuid: String): Flow<RecruitmentWithAgentWithRoleAndAbilities?>

    // -------- [ Active ] --------

    @Transaction
    @Query(
        """
        SELECT Contracts.* FROM Contracts
        INNER JOIN ContractContents ON Contracts.uuid = ContractContents.contractUuid
        INNER JOIN (
            SELECT uuid, startTime, endTime FROM Seasons
            UNION ALL
            SELECT uuid, startTime, endTime FROM Events
        ) AS Relation ON ContractContents.relationUuid = Relation.uuid
            AND datetime(Relation.startTime) < datetime(:currentIsoTime)
            AND datetime(Relation.endTime) > datetime(:currentIsoTime)
        ORDER BY endTime ASC
    """
    )
    fun getActiveByTime(currentIsoTime: String): Flow<List<ContractWithContentWithChaptersWithLevelsAndRewards>>

    @Transaction
    @Query(
        """
        SELECT * FROM AgentRecruitments
        WHERE datetime(startDate) < datetime(:currentIsoTime)
            AND datetime(endDate) > datetime(:currentIsoTime)
        ORDER BY endDate ASC
    """
    )
    fun getActiveRecruitmentsByTime(currentIsoTime: String): Flow<List<RecruitmentWithAgentWithRoleAndAbilities>>

    // -------- [ Inactive ] --------

    @Transaction
    @Query(
        """
        SELECT Contracts.* FROM Contracts
        INNER JOIN ContractContents ON Contracts.uuid = ContractContents.contractUuid
        INNER JOIN Seasons ON ContractContents.relationType = "Season"
            AND ContractContents.relationUuid = Seasons.uuid
            AND datetime(Seasons.startTime) < datetime(:currentIsoTime)
            AND datetime(Seasons.endTime) < datetime(:currentIsoTime)
        ORDER BY endTime DESC
    """
    )
    fun getInactiveSeasonsByTime(currentIsoTime: String): Flow<List<ContractWithContentWithChaptersWithLevelsAndRewards>>

    @Transaction
    @Query(
        """
        SELECT Contracts.* FROM Contracts
        INNER JOIN ContractContents ON Contracts.uuid = ContractContents.contractUuid
        INNER JOIN Events ON ContractContents.relationType = "Event"
            AND ContractContents.relationUuid = Events.uuid
            AND datetime(Events.startTime) < datetime(:currentIsoTime)
            AND datetime(Events.endTime) < datetime(:currentIsoTime)
        ORDER BY endTime DESC
    """
    )
    fun getInactiveEventsByTime(currentIsoTime: String): Flow<List<ContractWithContentWithChaptersWithLevelsAndRewards>>

    @Transaction
    @Query(
        """
        SELECT * FROM AgentRecruitments
        WHERE datetime(startDate) < datetime(:currentIsoTime)
            AND datetime(endDate) < datetime(:currentIsoTime)
        ORDER BY endDate DESC
    """
    )
    fun getInactiveRecruitmentsByTime(currentIsoTime: String): Flow<List<RecruitmentWithAgentWithRoleAndAbilities>>

    @Transaction
    @Query("SELECT * FROM Contracts")
    fun getAll(): Flow<List<ContractWithContentWithChaptersWithLevelsAndRewards>>

    // -------- [ Agent Gears ] --------

    @Transaction
    @Query(
        """
        SELECT Contracts.* FROM Contracts
        INNER JOIN ContractContents ON Contracts.uuid = ContractContents.contractUuid
        INNER JOIN Agents ON ContractContents.relationType = "Agent"
            AND ContractContents.relationUuid == Agents.uuid
        ORDER BY displayName ASC
    """
    )
    fun getAllGears(): Flow<List<ContractWithContentWithChaptersWithLevelsAndRewards>>
}